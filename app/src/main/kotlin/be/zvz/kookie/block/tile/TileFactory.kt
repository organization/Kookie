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
 * Copyright (C) 2021 - 2022 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.block.tile

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.world.World
import com.koloboke.collect.map.hash.HashObjObjMaps
import com.koloboke.collect.set.hash.HashObjSets

typealias TileClass = Class<out Tile>

object TileFactory {
    private val knownTiles: MutableMap<String, TileClass> = HashObjObjMaps.newMutableMap()
    private val saveNames: MutableMap<TileClass, String> = HashObjObjMaps.newMutableMap()

    @JvmStatic
    @JvmOverloads
    fun register(tileClass: TileClass, saveNames: List<String> = listOf()) {
        val names = HashObjSets.newMutableSet(saveNames).apply { add(tileClass.simpleName) }
        names.forEach { knownTiles[it] = tileClass }
        this.saveNames[tileClass] = saveNames.first()
    }

    @JvmStatic
    internal fun createFromData(world: World, nbt: CompoundTag): Tile? {
        val tileClass = knownTiles[nbt.getString(Tile.TAG_ID, "")] ?: return null
        val vec = Vector3(
            nbt.getInt(Tile.TAG_X),
            nbt.getInt(Tile.TAG_Y),
            nbt.getInt(Tile.TAG_Z)
        )

        return tileClass.getConstructor(World::class.java, Vector3::class.java)
            .newInstance(world, vec)
            .apply { readSaveData(nbt) } as Tile
    }

    @JvmStatic
    fun getSaveId(tileClass: TileClass): String =
        saveNames[tileClass] ?: throw IllegalArgumentException("Tile $tileClass is not registered")
}
