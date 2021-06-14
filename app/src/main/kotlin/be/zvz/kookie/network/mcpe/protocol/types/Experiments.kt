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
package be.zvz.kookie.network.mcpe.protocol.types

import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import com.koloboke.collect.map.hash.HashObjObjMaps

class Experiments(
    private val experiments: MutableMap<String, Boolean>,
    private val hasPreviouslyUsedExperiments: Boolean
) {

    fun getExperiments(): MutableMap<String, Boolean> = experiments

    fun hasPreviouslyUsedExperiments(): Boolean = hasPreviouslyUsedExperiments

    fun write(output: PacketSerializer) {
        experiments.forEach { (experimentName, value) ->
            output.putString(experimentName)
            output.putBoolean(value)
        }
        output.putBoolean(hasPreviouslyUsedExperiments)
    }

    companion object {
        @JvmStatic
        fun read(input: PacketSerializer): Experiments {
            val experiments: MutableMap<String, Boolean> = HashObjObjMaps.newMutableMap()
            for (i in 0 until input.getLInt()) {
                val experimentName = input.getString()
                val enabled = input.getBoolean()
                experiments[experimentName] = enabled
            }
            val hasPreviouslyUsedExperiments = input.getBoolean()
            return Experiments(experiments, hasPreviouslyUsedExperiments)
        }
    }
}
