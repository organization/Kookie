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
import be.zvz.kookie.network.mcpe.protocol.types.CacheableNbt

@ProtocolIdentify(ProtocolInfo.IDS.AVAILABLE_ACTOR_IDENTIFIERS_PACKET)
class AvailableActorIdentifiersPacket : DataPacket(), ClientboundPacket {

    lateinit var identifiers: CacheableNbt

    override fun decodePayload(input: PacketSerializer) {
        identifiers = CacheableNbt(input.getNbtCompoundRoot())
    }

    override fun encodePayload(output: PacketSerializer) {
        output.put(identifiers.encodedNbt)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleAvailableActorIdentifiers(this)
    }

    companion object {
        fun create(nbt: CacheableNbt): AvailableActorIdentifiersPacket = AvailableActorIdentifiersPacket().apply {
            this.identifiers = nbt
        }
    }
}
