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
package be.zvz.kookie.world.generator

import com.koloboke.collect.map.hash.HashObjObjMaps
import kotlin.reflect.KClass

object GeneratorManager {
    val list: MutableMap<String, KClass<out Generator>> = HashObjObjMaps.newMutableMap()

    init {
        addGenerator(NormalGenerator::class, "normal")
        // TODO: Add other generators
    }

    @JvmOverloads
    fun addGenerator(clazz: KClass<out Generator>, name: String, overwrite: Boolean = false) {
        if (!overwrite && list.containsKey(name.lowercase())) {
            throw IllegalArgumentException("Alias \"$name\" is already assigned")
        }
        list[name.lowercase()] = clazz
    }

    fun getGeneratorList() = list.keys

    @JvmOverloads
    fun getGenerator(name: String, throwOnMissing: Boolean = false): KClass<out Generator> =
        list[name.lowercase()] ?: if (throwOnMissing) {
            throw IllegalArgumentException("Alias \"$name\" does not map to any known generator")
        } else {
            NormalGenerator::class
        }

    fun getGeneratorName(clazz: KClass<out Generator>): String =
        list.keys.firstOrNull { list[it] === clazz }
            ?: throw IllegalArgumentException("Generator class ${clazz.qualifiedName} is not registered")
}
