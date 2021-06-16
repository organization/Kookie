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

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.CacheableNbt

@ProtocolIdentify(ProtocolInfo.IDS.BLOCK_ACTOR_DATA_PACKET)
class BlockActorDataPacket : DataPacket(), ClientboundPacket, ServerboundPacket {
    val pos = PacketSerializer.BlockPosition()
    lateinit var namedtag: CacheableNbt

    override fun decodePayload(input: PacketSerializer) {
        input.getBlockPosition(pos)
        namedtag = CacheableNbt(input.getNbtCompoundRoot())
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putBlockPosition(pos)
        output.put(namedtag.encodedNbt)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleBlockActorData(this)

    companion object {
        @JvmStatic
        fun create(pos: Vector3, nbt: CacheableNbt): BlockActorDataPacket =
            create(pos.x.toInt(), pos.y.toInt(), pos.z.toInt(), nbt)

        @JvmStatic
        fun create(pos: PacketSerializer.BlockPosition, nbt: CacheableNbt): BlockActorDataPacket =
            create(pos.x, pos.y, pos.z, nbt)

        @JvmStatic
        fun create(x: Int, y: Int, z: Int, nbt: CacheableNbt): BlockActorDataPacket = BlockActorDataPacket().apply {
            this.pos.x = x
            this.pos.y = y
            this.pos.z = z
            this.namedtag = nbt
        }
    }
}
