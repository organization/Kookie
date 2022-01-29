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

@ProtocolIdentify(ProtocolInfo.IDS.LAB_TABLE_PACKET)
class LabTablePacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    var type: Int = 0
    lateinit var position: PacketSerializer.BlockPosition
    var reactionType: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        type = input.getByte()
        input.getSignedBlockPosition(position)
        reactionType = input.getByte()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(type)
        output.putSignedBlockPosition(position)
        output.putByte(reactionType)
    }

    companion object {
        const val TYPE_START_COMBINE = 0
        const val TYPE_START_REACTION = 1
        const val TYPE_RESET = 2
    }
}
