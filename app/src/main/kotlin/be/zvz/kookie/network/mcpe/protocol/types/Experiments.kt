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
package be.zvz.kookie.network.mcpe.protocol.types

import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.utils.inline.forEachIndexed
import com.koloboke.collect.map.hash.HashObjObjMaps

class Experiments(
    private val experiments: MutableMap<String, Boolean>,
    private val hasPreviouslyUsedExperiments: Boolean
) {

    fun getExperiments(): MutableMap<String, Boolean> = experiments

    fun hasPreviouslyUsedExperiments(): Boolean = hasPreviouslyUsedExperiments

    fun write(output: PacketSerializer) {
        experiments.forEachIndexed(output::putString, output::putBoolean)
        output.putBoolean(hasPreviouslyUsedExperiments)
    }

    companion object {
        @JvmStatic
        fun read(input: PacketSerializer) = Experiments(
            experiments = run {
                val experiments: MutableMap<String, Boolean> = HashObjObjMaps.newMutableMap()
                repeat(input.getLInt()) {
                    val experimentName = input.getString()
                    val enabled = input.getBoolean()
                    experiments[experimentName] = enabled
                }
                experiments
            },
            hasPreviouslyUsedExperiments = input.getBoolean()
        )
    }
}
