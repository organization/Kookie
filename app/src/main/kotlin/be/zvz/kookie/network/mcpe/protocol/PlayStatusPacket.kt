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

@ProtocolIdentify(ProtocolInfo.IDS.PLAY_STATUS_PACKET)
class PlayStatusPacket : DataPacket(), ClientboundPacket {

    var status: PlayStatus = PlayStatus.LOGIN_SUCCESS

    override fun decodePayload(input: PacketSerializer) {
        status = PlayStatus.from(input.getInt())
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putInt(status.state)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handlePlayStatus(this)
    }

    override fun canBeSentBeforeLogin(): Boolean {
        return true
    }

    enum class PlayStatus(val state: Int) {
        LOGIN_SUCCESS(0),
        LOGIN_FAILED_CLIENT(1),
        LOGIN_FAILED_SERVER(2),
        PLAYER_SPAWN(3),
        LOGIN_FAILED_INVALID_TENANT(4),
        LOGIN_FAILED_VANILLA_EDU(5),
        LOGIN_FAILED_EDU_VANILLA(6),
        LOGIN_FAILED_SERVER_FULL(7);

        companion object {
            private val VALUES = values()
            fun from(value: Int) = VALUES.first { it.state == value }
        }
    }

    companion object {

        fun create(status: PlayStatus): PlayStatusPacket = PlayStatusPacket().apply {
            this.status = status
        }
    }
}
