/**
 *
 * _  __           _    _
 * | |/ /___   ___ | | _(_) ___
 * | ' // _ \ / _ \| |/ / |/ _ \
 * | . \ (_) | (_) |   <| |  __/
 * |_|\_\___/ \___/|_|\_\_|\___|
 *
 * A server software for Minecraft: Bedrock Edition
 *
 * Copyright (C) 2021 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.world

import be.zvz.kookie.Server
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.event.world.WorldInitEvent
import be.zvz.kookie.event.world.WorldLoadEvent
import be.zvz.kookie.event.world.WorldUnloadEvent
import be.zvz.kookie.lang.KnownTranslationKeys
import be.zvz.kookie.player.ChunkSelector
import be.zvz.kookie.timings.Timings
import be.zvz.kookie.world.format.io.WorldProviderManager
import be.zvz.kookie.world.format.io.WritableWorldProvider
import be.zvz.kookie.world.format.io.exception.CorruptedWorldException
import be.zvz.kookie.world.format.io.exception.UnsupportedWorldFormatException
import be.zvz.kookie.world.generator.GeneratorManager
import com.koloboke.collect.map.hash.HashIntObjMaps
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path
import java.nio.file.Paths

class WorldManager(
    private val server: Server,
    val dataPath: String,
    val providerManager: WorldProviderManager
) {
    private val worldPath: Path get() = server.dataPath.resolve("worlds")

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    val worlds: MutableMap<Int, World> = HashIntObjMaps.newMutableMap()
    var defaultWorld: World? = null

    var autoSave: Boolean = true
        set(value) {
            field = value
            worlds.values.forEach { world ->
                world.autoSave = true
            }
        }
    var autoSaveTicks = 6000
        set(value) {
            if (value <= 0) {
                throw IllegalArgumentException("Auto save ticks must be positive")
            }
            field = value
        }

    private var autoSaveTicker: Long = 0

    fun isWorldLoaded(name: String): Boolean = getWorldByName(name) is World
    fun getWorldByName(name: String): World? = worlds.values.find { it.folderName == name }
    fun getWorld(id: Int): World? = worlds[id]

    fun loadWorld(name: String): Boolean {
        if (name.trim() == "") {
            throw IllegalArgumentException("Invalid empty world name")
        }
        if (isWorldLoaded(name)) {
            return true
        } else if (!isWorldGenerated(name)) {
            return false
        }

        val path = getWorldPath(name)

        val providers = providerManager.getMatchingProviders(path)

        if (providers.size != 1) {
            logger.error(
                server.language.translateString(
                    KnownTranslationKeys.KOOKIE_LEVEL_LOADERROR,
                    listOf(
                        name,
                        if (providers.isEmpty()) {
                            server.language.translateString(
                                KnownTranslationKeys.KOOKIE_LEVEL_UNKNOWNFORMAT
                            )
                        } else {
                            server.language.translateString(
                                KnownTranslationKeys.KOOKIE_LEVEL_AMBIGUOUSFORMAT,
                                providers.keys.joinToString(", ")
                            )
                        }
                    )
                )
            )
            return false
        }
        val providerClass = providers.values.firstOrNull()
        if (providerClass == null) {
            logger.error(
                server.language.translateString(
                    KnownTranslationKeys.KOOKIE_LEVEL_UNKNOWNFORMAT
                )
            )
            return false
        }
        try {
            val provider = providerClass.fromPath(path)
            try {
                GeneratorManager.getGenerator(provider.worldData.generatorName, true)
            } catch (e: IllegalArgumentException) {
                logger.error(
                    server.language.translateString(
                        KnownTranslationKeys.KOOKIE_LEVEL_LOADERROR,
                        listOf(
                            name,
                            "Unknown generator \"${provider.worldData.generatorName}\""
                        )
                    )
                )
                return false
            }

            if (provider !is WritableWorldProvider) {
                // TODO: Convert support
                throw UnsupportedWorldFormatException("World \"$name\" is in an unsupported format and needs to be upgraded")
            }

            val world = World(server, name, provider, server.asyncPool)
            worlds[world.id] = world
            world.autoSave = autoSave

            WorldLoadEvent(world).call()
            return true
        } catch (e: CorruptedWorldException) {
            logger.error(
                server.language.translateString(
                    KnownTranslationKeys.KOOKIE_LEVEL_LOADERROR,
                    listOf(
                        name,
                        "Corrupted data detected: ${e.message}"
                    )
                )
            )
            return false
        } catch (e: UnsupportedWorldFormatException) {
            logger.error(
                server.language.translateString(
                    KnownTranslationKeys.KOOKIE_LEVEL_LOADERROR,
                    listOf(
                        name,
                        "Unsupported format: ${e.message}"
                    )
                )
            )
            return false
        }
    }

    @JvmOverloads
    fun generateWorld(name: String, options: WorldCreationOptions, backgroundCreation: Boolean = true): Boolean {
        if (name.trim() == "" || isWorldGenerated(name)) {
            return false
        }
        val providerEntry = providerManager.default
        val path = getWorldPath(name)
        providerEntry.generate(path, name, options)

        // providerEntry is allowed to set only WritableWorldProvider, we don't have to check if provider is WritableWorldProvider
        val world = World(server, name, providerEntry.fromPath(path) as WritableWorldProvider, server.asyncPool)
        worlds[world.id] = world
        world.autoSave = autoSave

        WorldInitEvent(world).call()

        WorldLoadEvent(world).call()

        if (backgroundCreation) {
            logger.info(
                server.language.translateString(
                    KnownTranslationKeys.KOOKIE_LEVEL_BACKGROUNDGENERATION,
                    listOf(name)
                )
            )
            val spawnLocation = world.spawnLocation

            val centerX = spawnLocation.chunkX
            val centerZ = spawnLocation.chunkZ

            val selected = ChunkSelector().selectChunks(8, centerX, centerZ)
            var done = 0
            val total = selected.toList().size
            selected.forEach { index ->
                val (chunkX, chunkZ) = World.parseChunkHash(index)
                world.orderChunkPopulation(chunkX, chunkZ, null).onCompletion(
                    {
                        val oldProgress = (done / total) * 100
                        val newProgress = (++done / total) * 100
                        if (oldProgress != newProgress || done == total || done == 1) {
                            world.logger.info("Generating spawn terrain chunks: $done / $total ($newProgress%)")
                        }
                    },
                    {
                        // NOOP: we don't care if the world was unloaded
                    }
                )
            }
        }
        return true
    }

    @JvmOverloads
    fun unloadWorld(world: World, forceUnload: Boolean = false): Boolean {
        if (world == defaultWorld && !forceUnload) {
            throw IllegalArgumentException("The default world cannot be unloaded while running, please switch worlds")
        }
        if (world.doingTick) {
            throw IllegalArgumentException("Cannot unload a world during world tick")
        }
        val ev = WorldUnloadEvent(world)
        if (world == defaultWorld && !forceUnload) {
            ev.isCancelled = true
        }
        ev.call()

        if (!forceUnload && ev.isCancelled) {
            return false
        }
        logger.info(
            server.language.translateString(
                KnownTranslationKeys.KOOKIE_LEVEL_UNLOADING,
                listOf(world.displayName)
            )
        )
        try {
            val safeSpawn = defaultWorld?.getSafeSpawn()
            val iterator = world.players.iterator()
            // We can do this using world.players.forEach, but player.teleport() will remove player from current world.
            while (iterator.hasNext()) {
                val (_, player) = iterator.next()
                if (world == defaultWorld || safeSpawn == null) {
                    // TODO: player.disconnect("Forced default world unload")
                } else {
                    player.teleport(safeSpawn)
                }
            }
            if (world == defaultWorld) {
                defaultWorld = null
            }
            worlds.remove(world.id)
            world.onUnload()
            return true
        } catch (e: WorldException) {
            throw WorldException("Getting world spawn failed: ${e.message}")
        }
    }

    fun getWorldPath(name: String): Path = Paths.get(worldPath.toString(), name)

    fun isWorldGenerated(name: String): Boolean =
        if (name.trim() == "") {
            false
        } else if ((getWorldByName(name)) !is World) {
            providerManager.getMatchingProviders(Paths.get(worldPath.toString(), name)).isNotEmpty()
        } else {
            true
        }

    fun findEntity(entityId: Long): Entity? {
        worlds.forEach { (_, world) ->
            assert(world.isLoaded())
            val entity = world.getEntity(entityId)
            if (entity is Entity) {
                return entity
            }
        }
        return null
    }

    fun tick(currentTick: Long) {
        worlds.forEach { (k, world) ->
            if (worlds[k] == null) {
                // World unloaded during the tick of a world earlier in this loop, perhaps by plugin
                return@forEach
            }
            val worldTime = System.currentTimeMillis()
            world.doTick(currentTick)
            val tickMs = (System.currentTimeMillis() - worldTime)
            if (tickMs >= 50) {
                world.logger.debug("Tick took too long: ${tickMs}s (" + (tickMs / 50).toString() + ")")
            }
        }
        if (autoSave && ++autoSaveTicker >= autoSaveTicks) {
            autoSaveTicker = 0
            logger.debug("[Auto Save] Saving worlds...")
            val start = System.currentTimeMillis()
            doAutoSave()
            val time = System.currentTimeMillis() - start
            logger.debug(
                "[Auto Save] Save completed in " + if (time >= 1000) {
                    "${(time / 1000)}s"
                } else {
                    "${time}ms"
                }
            )
        }
    }

    private fun doAutoSave() {
        Timings.worldSave.startTiming()
        worlds.forEach { (_, world) ->
            world.players.forEach { (_, player) ->
                /*
                TODO:
                if (player.spawned) {
                    player.save()
                }
                 */
            }
            world.save(false)
        }
        Timings.worldSave.stopTiming()
    }
}
