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

class ResourcePackDataInfoPacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.RESOURCE_PACK_DATA_INFO_PACKET)

	var packId: string
	var maxChunkSize: Int
	var chunkCount: Int
	var compressedPackSize: Int
	var sha: string256
	var isPremium: Boolean = false
	var packType: Int = ResourcePackType::RESOURCES //TODO: check the values for this

	 static fun create(packId: string, maxChunkSize: Int, chunkCount: Int, compressedPackSize: Int, sha256sum: string) : self{
		result = new self
		result.packId = packId
		result.maxChunkSize = maxChunkSize
		result.chunkCount = chunkCount
		result.compressedPackSize = compressedPackSize
		result.sha256 = sha256sum
		return result
	}

	override fun decodePayload(input: PacketSerializer) {
		packId = input.getString()
		maxChunkSize = input.getLInt()
		chunkCount = input.getLInt()
		compressedPackSize = input.getLLong()
		sha256 = input.getString()
		isPremium = input.getBool()
		packType = input.getByte()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putString(packId)
		output.putLInt(maxChunkSize)
		output.putLInt(chunkCount)
		output.putLLong(compressedPackSize)
		output.putString(sha256)
		output.putBool(isPremium)
		output.putByte(packType)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleResourcePackDataInfo(this)
	}
}
