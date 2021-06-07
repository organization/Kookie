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
package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.MULTIPLAYER_SETTINGS_PACKET)
class MultiplayerSettingsPacket : DataPacket(), ClientboundPacket, ServerboundPacket {
    lateinit var action: Action

    companion object {
        fun create(action: Action): MultiplayerSettingsPacket = MultiplayerSettingsPacket().apply {
            this.action = action
        }
    }

    override fun decodePayload(input: PacketSerializer) {
        action = Action.from(input.getVarInt())
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putVarInt(action.value)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleMultiplayerSettings(this)
    }

    enum class Action(val value: Int) {
        ENABLE_MULTIPLAYER(0),
        DISABLE_MULTIPLAYER(1),
        REFRESH_JOIN_CODE(2);

        companion object {
            private val VALUES = values()
            fun from(value: Int) = VALUES.first { it.value == value }
        }
    }
}
