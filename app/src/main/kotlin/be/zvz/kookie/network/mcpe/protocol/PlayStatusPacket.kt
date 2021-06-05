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

    var status: Int = LOGIN_SUCCESS

    override fun decodePayload(input: PacketSerializer) {
        status = input.getInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putInt(status)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handlePlayStatus(this)
    }

    override fun canBeSentBeforeLogin(): Boolean {
        return true
    }

    companion object {
        const val LOGIN_SUCCESS = 0
        const val LOGIN_FAILED_CLIENT = 1
        const val LOGIN_FAILED_SERVER = 2
        const val PLAYER_SPAWN = 3
        const val LOGIN_FAILED_INVALID_TENANT = 4
        const val LOGIN_FAILED_VANILLA_EDU = 5
        const val LOGIN_FAILED_EDU_VANILLA = 6
        const val LOGIN_FAILED_SERVER_FULL = 7

        fun create(status: Int): PlayStatusPacket = PlayStatusPacket().apply {
            this.status = status
        }
    }
}
