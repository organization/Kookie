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

class BooleanGameRule(private val value: Boolean, override val playerModifiable: Boolean) : GameRule(playerModifiable) {

    override val typeId = GameRuleType.BOOL

    fun getValue(): Boolean = value

    override fun encode(output: PacketSerializer) {
        output.putBoolean(value)
    }

    companion object {
        fun decode(input: PacketSerializer, playerModifiable: Boolean) =
            BooleanGameRule(input.getBoolean(), playerModifiable)
    }
}
