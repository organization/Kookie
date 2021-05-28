package be.zvz.kookie.network.mcpe.protocol

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
use fun count

class PurchaseReceiptPacket : DataPacket(), ServerboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.PURCHASE_RECEIPT_PACKET)

	/** @var string[] */
	 entries = []

	override fun decodePayload(input: PacketSerializer) {
		count = input.getUnsignedVarInt()
		for(i = 0 i < count ++i){
			entries[] = input.getString()
		}
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putUnsignedVarInt(count(entries))
		foreach(entries entry: as){
			output.putString(entry)
		}
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handlePurchaseReceipt(this)
	}
}
