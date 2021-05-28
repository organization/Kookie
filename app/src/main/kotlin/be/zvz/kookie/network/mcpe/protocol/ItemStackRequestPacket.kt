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

class ItemStackRequestPacket : DataPacket(), ServerboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.ITEM_STACK_REQUEST_PACKET)

	/** @var ItemStackRequest[] */
	 requests

	/**
	 * @param ItemStackRequest[] requests
	 */
	 static fun create(requests: array) : self{
		result = new self
		result.requests = requests
		return result
	}

	/** @return ItemStackRequest[] */
	 fun getRequests() : array{ return requests }

	override fun decodePayload(input: PacketSerializer) {
		requests = []
		for(i = 0, len = input.getUnsignedVarInt() i < len ++i){
			requests[] = ItemStackRequest::read(input)
		}
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putUnsignedVarInt(count(requests))
		foreach(requests request: as){
			request.write(output)
		}
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleItemStackRequest(this)
	}
}
