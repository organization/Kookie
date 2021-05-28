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

class ResourcePackChunkDataPacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.RESOURCE_PACK_CHUNK_DATA_PACKET)

	var packId: string
	var chunkIndex: Int
	var progress: Int
	var data: string

	 static fun create(packId: string, chunkIndex: Int, chunkOffset: Int, data: string) : self{
		result = new self
		result.packId = packId
		result.chunkIndex = chunkIndex
		result.progress = chunkOffset
		result.data = data
		return result
	}

	override fun decodePayload(input: PacketSerializer) {
		packId = input.getString()
		chunkIndex = input.getLInt()
		progress = input.getLLong()
		data = input.getString()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putString(packId)
		output.putLInt(chunkIndex)
		output.putLLong(progress)
		output.putString(data)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleResourcePackChunkData(this)
	}
}
