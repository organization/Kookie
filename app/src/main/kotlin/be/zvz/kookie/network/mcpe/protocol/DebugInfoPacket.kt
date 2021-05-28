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

class DebugInfoPacket : DataPacket(), ClientboundPacket, ServerboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.DEBUG_INFO_PACKET)

	var entityUniqueId: Int
	var data: string

	 static fun create(entityUniqueId: Int, data: string) : self{
		result = new self
		result.entityUniqueId = entityUniqueId
		result.data = data
		return result
	}

	/**
	 * TODO: we can't call this getEntityRuntimeId() because of base class collision (crap architecture, thanks Shoghi)
	 */
	 fun getEntityUniqueIdField() : Int{ return entityUniqueId }

	 fun getData() : string{ return data }

	override fun decodePayload(input: PacketSerializer) {
		entityUniqueId = input.getEntityUniqueId()
		data = input.getString()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putEntityUniqueId(entityUniqueId)
		output.putString(data)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleDebugInfo(this)
	}
}
