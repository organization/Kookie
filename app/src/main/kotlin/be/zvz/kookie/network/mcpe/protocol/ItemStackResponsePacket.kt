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

class ItemStackResponsePacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.ITEM_STACK_RESPONSE_PACKET)

	/** @var ItemStackResponse[] */
	 responses

	/**
	 * @param ItemStackResponse[] responses
	 */
	 static fun create(responses: array) : self{
		result = new self
		result.responses = responses
		return result
	}

	/** @return ItemStackResponse[] */
	 fun getResponses() : array{ return responses }

	override fun decodePayload(input: PacketSerializer) {
		responses = []
		for(i = 0, len = input.getUnsignedVarInt() i < len ++i){
			responses[] = ItemStackResponse::read(input)
		}
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putUnsignedVarInt(count(responses))
		foreach(responses response: as){
			response.write(output)
		}
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleItemStackResponse(this)
	}
}
