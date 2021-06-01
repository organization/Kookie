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
import be.zvz.kookie.network.mcpe.protocol.types.CacheableNbt
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer
import java.util.concurrent.atomic.AtomicInteger

@ProtocolIdentify(ProtocolInfo.IDS.BLOCK_ACTOR_DATA_PACKET)
class BlockActorDataPacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    var x: AtomicInteger = AtomicInteger()
    var y: AtomicInteger = AtomicInteger()
    var z: AtomicInteger = AtomicInteger()

    lateinit var namedtag: CacheableNbt

    override fun decodePayload(input: PacketSerializer) {
        input.getBlockPosition(x, y, z)
        namedtag = CacheableNbt(input.getNbtCompoundRoot())
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putBlockPosition(x.get(), y.get(), z.get())
        output.put(namedtag.encodedNbt)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleBlockActorData(this)
    }

    companion object {
        fun create(x: Int, y: Int, z: Int, nbt: CacheableNbt): BlockActorDataPacket = BlockActorDataPacket().apply {
            this.namedtag = nbt
            this.x.set(x)
            this.y.set(y)
            this.z.set(z)
        }
    }
}
