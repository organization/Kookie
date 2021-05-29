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

@ProtocolIdentify(ProtocolInfo.IDS.BIOME_DEFINITION_LIST_PACKET)
class BiomeDefinitionListPacket : DataPacket(), ClientboundPacket {

    /**
     * @var CacheableNbt
     * @phpstan-var CacheableNbt<\pocketmine\nbt\tag\CompoundTag>
     */
    defs

    /**
     * @phpstan-param CacheableNbt<\pocketmine\nbt\tag\CompoundTag> nbt
     */
    static
    fun create(nbt: CacheableNbt): self {
        result = new self
                result.defs = nbt
        return result
    }

    override fun decodePayload(input: PacketSerializer) {
        defs = new CacheableNbt (input.getNbtCompoundRoot())
    }

    override fun encodePayload(output: PacketSerializer) {
        output.put(defs->getEncodedNbt())
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleBiomeDefinitionList(this)
    }
}
