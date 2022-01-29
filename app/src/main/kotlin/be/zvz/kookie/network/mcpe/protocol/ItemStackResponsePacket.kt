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
import be.zvz.kookie.network.mcpe.protocol.types.inventory.stackresponse.ItemStackResponse

@ProtocolIdentify(ProtocolInfo.IDS.ITEM_STACK_RESPONSE_PACKET)
class ItemStackResponsePacket : DataPacket(), ClientboundPacket {

    lateinit var responses: MutableList<ItemStackResponse>

    override fun decodePayload(input: PacketSerializer) {
        val responses = mutableListOf<ItemStackResponse>()
        repeat(input.getUnsignedVarInt()) {
            responses.add(ItemStackResponse.read(input))
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        responses.forEach {
            it.write(output)
        }
    }

    companion object {
        @JvmStatic
        fun create(responses: MutableList<ItemStackResponse>) = ItemStackResponsePacket().apply {
            this.responses = responses
        }
    }
}
