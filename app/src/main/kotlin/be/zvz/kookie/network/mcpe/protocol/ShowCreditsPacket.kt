package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

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

@ProtocolIdentify(ProtocolInfo.IDS.SHOW_CREDITS_PACKET)
class ShowCreditsPacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    var playerEid: Long = 0
    var status: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        playerEid = input.getEntityRuntimeId()
        status = input.getVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityRuntimeId(playerEid)
        output.putVarInt(status)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleShowCredits(this)

    companion object {
        const val STATUS_START_CREDITS = 0
        const val STATUS_END_CREDITS = 1
    }
}
