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

import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.BLOCK_ACTOR_DATA_PACKET)
class BlockActorDataPacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    var x: Int
    var y: Int
    var z: Int
    /**
     * @var CacheableNbt
     * @phpstan-var CacheableNbt<\pocketmine\nbt\tag\CompoundTag>
     */
    namedtag

    /**
     * @phpstan-param CacheableNbt<\pocketmine\nbt\tag\CompoundTag> nbt
     */
    static
    fun create(x: Int, y: Int, z: Int, nbt: CacheableNbt): self {
        result = new self
                [result.x, result.y, result.z] = [x, y, z]
        result.namedtag = nbt
        return result
    }

    override fun decodePayload(input: PacketSerializer) {
        input.getBlockPosition(x, y, z)
        namedtag = new CacheableNbt (input.getNbtCompoundRoot())
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putBlockPosition(x, y, z)
        output.put(namedtag->getEncodedNbt())
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleBlockActorData(this)
    }
}
