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

@ProtocolIdentify(ProtocolInfo.IDS.PURCHASE_RECEIPT_PACKET)
class PurchaseReceiptPacket : DataPacket(), ServerboundPacket {
    val entries = mutableListOf<String>()

    override fun decodePayload(input: PacketSerializer) {
        val count = input.getUnsignedVarInt()
        for (i in 0 until count) {
            entries.add(input.getString())
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putUnsignedVarInt(entries.size)
        entries.forEach { entry ->
            output.putString(entry)
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handlePurchaseReceipt(this)
}
