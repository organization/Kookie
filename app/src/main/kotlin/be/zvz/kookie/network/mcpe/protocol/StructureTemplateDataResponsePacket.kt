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

class StructureTemplateDataResponsePacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.STRUCTURE_TEMPLATE_DATA_RESPONSE_PACKET)

	var structureTemplateName: string
	/**
	 * @var CacheableNbt|null
	 * @phpstan-var CacheableNbt<\pocketmine\nbt\tag\CompoundTag>
	 */
	 namedtag

	override fun decodePayload(input: PacketSerializer) {
		structureTemplateName = input.getString()
		if(input.getBool()){
			namedtag = new CacheableNbt(input.getNbtCompoundRoot())
		}
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putString(structureTemplateName)
		output.putBool(namedtag !== null)
		if(namedtag !== null){
			output.put(namedtag->getEncodedNbt())
		}
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleStructureTemplateDataResponse(this)
	}
}
