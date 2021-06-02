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
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer
import java.util.concurrent.atomic.AtomicInteger

@ProtocolIdentify(ProtocolInfo.IDS.UPDATE_BLOCK_PACKET)
class UpdateBlockPacket : DataPacket(), ClientboundPacket {

    var x: AtomicInteger = AtomicInteger()
    var y: AtomicInteger = AtomicInteger()
    var z: AtomicInteger = AtomicInteger()
    var blockRuntimeId: Int = 0

    /**
     * Flags are used by MCPE internally for block setting, but only flag 2 (network flag) is relevant for network.
     * This field is pointless really.
     */
    var flags: Int = 0x02
    var dataLayerId: Int = DATA_LAYER_NORMAL

    override fun decodePayload(input: PacketSerializer) {
        input.getBlockPosition(x, y, z)
        blockRuntimeId = input.getUnsignedVarInt()
        flags = input.getUnsignedVarInt()
        dataLayerId = input.getUnsignedVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putBlockPosition(x.get(), y.get(), z.get())
        output.putUnsignedVarInt(blockRuntimeId)
        output.putUnsignedVarInt(flags)
        output.putUnsignedVarInt(dataLayerId)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleUpdateBlock(this)
    }

    companion object {
        const val DATA_LAYER_NORMAL = 0
        const val DATA_LAYER_LIQUID = 1
    }
}
