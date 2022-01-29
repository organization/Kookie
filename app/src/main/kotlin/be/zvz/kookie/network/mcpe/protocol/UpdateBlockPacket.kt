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

@ProtocolIdentify(ProtocolInfo.IDS.UPDATE_BLOCK_PACKET)
open class UpdateBlockPacket : DataPacket(), ClientboundPacket {
    val pos = PacketSerializer.BlockPosition()
    var blockRuntimeId: Int = 0

    /**
     * Flags are used by MCPE internally for block setting, but only flag 2 (network flag) is relevant for network.
     * This field is pointless really.
     */
    var flags: Int = 0x02
    var dataLayerId: Int = DATA_LAYER_NORMAL

    override fun decodePayload(input: PacketSerializer) {
        input.getBlockPosition(pos)
        blockRuntimeId = input.getUnsignedVarInt()
        flags = input.getUnsignedVarInt()
        dataLayerId = input.getUnsignedVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putBlockPosition(pos)
        output.putUnsignedVarInt(blockRuntimeId)
        output.putUnsignedVarInt(flags)
        output.putUnsignedVarInt(dataLayerId)
    }

    companion object {
        const val DATA_LAYER_NORMAL = 0
        const val DATA_LAYER_LIQUID = 1
    }
}
