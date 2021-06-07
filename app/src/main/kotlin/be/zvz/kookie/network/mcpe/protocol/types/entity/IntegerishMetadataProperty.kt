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
package be.zvz.kookie.network.mcpe.protocol.types.entity

interface IntegerishMetadataProperty {
    var value: Int

    fun initialize(v: Int) {
        if (v < min() || v > max()) {
            throw AssertionError("Value is out of range ${min()} - ${max()}")
        }
        value = v
    }

    fun min(): Int

    fun max(): Int

    fun buildFromFlags(flags: Map<Int, Boolean>): IntegerishMetadataProperty
}
