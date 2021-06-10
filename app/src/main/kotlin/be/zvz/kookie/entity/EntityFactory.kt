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
package be.zvz.kookie.entity

import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.StringTag
import be.zvz.kookie.world.World
import com.koloboke.collect.map.hash.HashObjObjMaps

object EntityFactory {
    private val creationFuncs: MutableMap<String, (world: World, nbt: CompoundTag) -> Entity> = HashObjObjMaps.newMutableMap()

    private val saveNames: MutableMap<Class<out Entity>, String> = HashObjObjMaps.newMutableMap()

    init {
        // TODO: register default entities on here
    }

    fun register(
        clazz: Class<out Entity>,
        creationFunc: (world: World, nbt: CompoundTag) -> Entity,
        saveNames: List<String>
    ) {
        if (saveNames.isEmpty()) {
            throw IllegalArgumentException("At least one save name must be provided")
        }
        saveNames.forEach {
            creationFuncs[it] = creationFunc
        }

        this.saveNames[clazz] = saveNames.first()
    }

    fun createFromData(world: World, nbt: CompoundTag): Entity? {
        val saveId = nbt.getTag("id")
        return if (saveId is StringTag) {
            creationFuncs[saveId.value]?.invoke(world, nbt)
        } else {
            null
        }
    }

    fun injectSaveId(clazz: Class<out Entity>, saveData: CompoundTag) {
        if (saveNames.containsKey(clazz)) {
            saveData.setTag("id", StringTag(saveNames.getValue(clazz)))
        } else {
            throw IllegalArgumentException("Entity ${clazz.simpleName} is not registered")
        }
    }

    fun getSaveId(clazz: Class<out Entity>): String = if (saveNames.containsKey(clazz)) {
        saveNames.getValue(clazz)
    } else {
        throw IllegalArgumentException("Entity ${clazz.simpleName} is not registered")
    }
}
