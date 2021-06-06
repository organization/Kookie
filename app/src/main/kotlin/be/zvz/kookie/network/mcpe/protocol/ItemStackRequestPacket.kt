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
import be.zvz.kookie.network.mcpe.protocol.types.inventory.stackrequest.ItemStackRequest

@ProtocolIdentify(ProtocolInfo.IDS.ITEM_STACK_REQUEST_PACKET)
class ItemStackRequestPacket : DataPacket(), ServerboundPacket {
    lateinit var requests: MutableList<ItemStackRequest>

    override fun decodePayload(input: PacketSerializer) {
        requests.clear()
        for (i in 0 until input.getUnsignedVarInt()) {
            requests.add(ItemStackRequest.read(input))
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putUnsignedVarInt(requests.size)
        requests.forEach {
            it.write(output)
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleItemStackRequest(this)
    }

    companion object {
        fun create(requests: MutableList<ItemStackRequest>) = ItemStackRequestPacket().apply {
            this.requests = requests
        }
    }
}
