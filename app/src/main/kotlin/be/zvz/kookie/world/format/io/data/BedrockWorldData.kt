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
package be.zvz.kookie.world.format.io.data

import be.zvz.kookie.Server
import be.zvz.kookie.nbt.LittleEndianNbtSerializer
import be.zvz.kookie.nbt.NbtDataException
import be.zvz.kookie.nbt.TreeRoot
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.IntTag
import be.zvz.kookie.nbt.tag.StringTag
import be.zvz.kookie.utils.Binary
import be.zvz.kookie.world.WorldCreationOptions
import be.zvz.kookie.world.format.io.exception.CorruptedWorldException
import be.zvz.kookie.world.format.io.exception.UnsupportedWorldFormatException
import be.zvz.kookie.world.generator.FlatGenerator
import be.zvz.kookie.world.generator.GeneratorManager
import java.nio.file.Path
import java.util.zip.DeflaterOutputStream
import java.util.zip.InflaterInputStream
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

class BedrockWorldData(dataPath: Path) : BaseNbtWorldData(dataPath) {
    @Throws(UnsupportedWorldFormatException::class)
    override fun fix() {
        val generatorNameTag = compoundTag.getTag("generatorName")
        if (generatorNameTag !is StringTag) {
            (compoundTag.getTag("Generator") as? IntTag)?.let {
                when (it.value) {
                    GENERATOR_FLAT -> {
                        compoundTag.setString("generatorName", "flat")
                        compoundTag.setString("generatorOptions", "2;7,3,3,2;1")
                    }
                    GENERATOR_INFINITE -> {
                        compoundTag.setString("generatorName", "default")
                        compoundTag.setString("generatorOptions", "")
                    }
                    GENERATOR_LIMITED -> throw UnsupportedWorldFormatException("Limited worlds are not currently supported")
                    else -> throw UnsupportedWorldFormatException("Unknown LevelDB generator type")
                }
            } ?: compoundTag.setString("generatorName", "default")
        }

        if (compoundTag.getTag("generatorOptions") !is StringTag) {
            compoundTag.setString("generatorOptions", "")
        }
    }

    @Throws(CorruptedWorldException::class, UnsupportedWorldFormatException::class)
    override fun load(): CompoundTag {
        var rawLevelData: String = InflaterInputStream(dataPath.inputStream()).readBytes().toString()
        if (rawLevelData.isEmpty()) {
            throw CorruptedWorldException("Failed to read level.dat (permission denied or doesn't exist)")
        }

        rawLevelData = rawLevelData.drop(8)
        if (rawLevelData.isEmpty()) {
            throw CorruptedWorldException("Truncated level.dat")
        }

        val worldData: CompoundTag = try {
            LittleEndianNbtSerializer().read(rawLevelData).mustGetCompoundTag()
        } catch (e: NbtDataException) {
            throw CorruptedWorldException(e.message ?: "Failed to read level.dat (nbt read failed)", e)
        }
        val version = worldData.getInt("StorageVersion", Int.MAX_VALUE)
        if (version > CURRENT_STORAGE_VERSION) {
            throw UnsupportedWorldFormatException("LevelDB world format version version is currently unsupported")
        }

        return worldData
    }

    override fun save() {
        compoundTag.setInt("NetworkVersion", Server.currentVersion.protocolVersion)
        compoundTag.setInt("StorageVersion", CURRENT_STORAGE_VERSION)

        val rawLevelData = appendHeader(LittleEndianNbtSerializer().write(TreeRoot(compoundTag)))
        DeflaterOutputStream(dataPath.outputStream()).write(rawLevelData.toByteArray())
    }

    companion object {
        const val CURRENT_STORAGE_VERSION = 8

        const val GENERATOR_LIMITED = 0
        const val GENERATOR_INFINITE = 1
        const val GENERATOR_FLAT = 2

        val versionBuffer: String by lazy { Binary.writeLInt(CURRENT_STORAGE_VERSION) }
        private fun appendHeader(buffer: String) = versionBuffer + Binary.writeLInt(buffer.length) + buffer

        @JvmStatic
        fun generate(dataPath: Path, name: String, options: WorldCreationOptions) {
            val worldData = TreeRoot(
                CompoundTag().apply {
                    setInt("DayCycleStopTime", -1)
                    setInt("Difficulty", options.difficulty)
                    setByte("ForceGameType", 0)
                    setInt("GameType", 0)
                    setInt(
                        "Generator",
                        if (options.generatorClass == FlatGenerator::class) GENERATOR_FLAT else GENERATOR_INFINITE
                    )
                    setLong("LastPlayed", System.currentTimeMillis())
                    setString("LevelName", name)
                    setInt("NetworkVersion", Server.currentVersion.protocolVersion)
                    setLong("RandomSeed", options.seed)
                    setInt("SpawnX", options.spawnPosition.floorX)
                    setInt("SpawnY", options.spawnPosition.floorY)
                    setInt("SpawnZ", options.spawnPosition.floorZ)
                    setInt("StorageVersion", CURRENT_STORAGE_VERSION)
                    setLong("Time", 0)
                    setByte("eduLevel", 0)
                    setByte("falldamage", 1)
                    setByte("firedamage", 1)
                    setByte("hasBeenLoadedInCreative", 1)
                    setByte("immutableWorld", 0)
                    setFloat("lightningLevel", 0F)
                    setInt("lightningTime", 0)
                    setByte("pvp", 1)
                    setFloat("rainLevel", 0F)
                    setInt("rainTime", 0)
                    setByte("spawnMobs", 1)
                    setByte("texturePacksRequired", 0)

                    // Additional PocketMine-MP fields
                    setTag("GameRules", CompoundTag())
                    setByte("hardcore", 0)
                    setString("generatorName", GeneratorManager.getGeneratorName(options.generatorClass))
                    setString("generatorOptions", options.generatorOptions)
                }
            )
            val rawLevelData = appendHeader(LittleEndianNbtSerializer().write(worldData))
            DeflaterOutputStream(dataPath.resolve("level.dat").outputStream()).write(rawLevelData.toByteArray())
        }
    }
}
