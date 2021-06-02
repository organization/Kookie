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
import be.zvz.kookie.network.mcpe.protocol.types.inventory.*
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.INVENTORY_TRANSACTION_PACKET)
class InventoryTransactionPacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    var requestId: Int = -1

    var requestChangedSlot = mutableListOf<InventoryTransactionChangedSlotsHack>()

    lateinit var trData: TransactionData

    override fun decodePayload(input: PacketSerializer) {
        requestId = input.readGenericTypeNetworkId()
        if (requestId != 0) {
            for (i in 0..input.getUnsignedVarInt()) {
                requestChangedSlot.add(InventoryTransactionChangedSlotsHack.read(input))
            }
        }

        val transactionType = input.getUnsignedVarInt()

        trData = when (transactionType) {
            TYPE_NORMAL -> NormalTransactionData()
            TYPE_MISMATCH -> MismatchTransactionData()
            TYPE_USE_ITEM -> UseItemTransactionData()
            TYPE_USE_ITEM_ON_ENTITY -> UseItemOnEntityTransactionData()
            else -> throw PacketDecodeException("Unknown transaction type $transactionType")
        }
        trData.decode(input)
    }

    override fun encodePayload(output: PacketSerializer) {
        output.writeGenericTypeNetworkId(requestId)
        if (requestId != 0) {
            output.putUnsignedVarInt(requestChangedSlot.size)
            requestChangedSlot.forEach {
                it.write(output)
            }
        }
        output.putUnsignedVarInt(trData.typeId)
        trData.encode(output)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleInventoryTransaction(this)
    }

    companion object {
        const val TYPE_NORMAL = 0
        const val TYPE_MISMATCH = 1
        const val TYPE_USE_ITEM = 2
        const val TYPE_USE_ITEM_ON_ENTITY = 3
        const val TYPE_RELEASE_ITEM = 4
    }
}
