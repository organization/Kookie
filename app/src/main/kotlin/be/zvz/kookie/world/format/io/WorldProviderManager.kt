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
package be.zvz.kookie.world.format.io

import be.zvz.kookie.world.format.io.leveldb.LevelDB
import com.koloboke.collect.map.hash.HashObjObjMaps
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.isDirectory

class WorldProviderManager {

    private val providers: MutableMap<String, WorldProviderManagerEntry> = HashObjObjMaps.newMutableMap()

    val default: WriteableWorldProviderManagerEntry = WriteableWorldProviderManagerEntry(
        { pathString ->
            val path = Paths.get(pathString)
            return@WriteableWorldProviderManagerEntry path.resolve("level.dat").toFile().isFile && path.resolve("db")
                .isDirectory()
        },
        { pathString -> return@WriteableWorldProviderManagerEntry LevelDB(Paths.get(pathString)) },
        { pathString, name, options ->
            return@WriteableWorldProviderManagerEntry LevelDB.generate(Paths.get(pathString), name, options)
        }
    )

    @JvmOverloads
    fun addProvider(providerEntry: WorldProviderManagerEntry, providerName: String, overwrite: Boolean = false) {
        val name = providerName.lowercase()
        if (providers.containsKey(name) && !overwrite) {
            throw IllegalArgumentException("Alias \"$name\" is already registered")
        }
        providers[name] = providerEntry
    }

    fun getMatchingProviders(path: Path): Map<String, WorldProviderManagerEntry> {
        val result: MutableMap<String, WorldProviderManagerEntry> = HashObjObjMaps.newMutableMap()

        providers.forEach { (providerName, provider) ->
            if (provider.isValid(path)) {
                result[providerName] = provider
            }
        }
        return result
    }

    fun getAvailableProviders(): MutableMap<String, WorldProviderManagerEntry> {
        return providers
    }

    fun getProviderByName(providerName: String): WorldProviderManagerEntry? {
        return providers[providerName]
    }
}
