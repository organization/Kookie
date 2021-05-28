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

class LevelChunkPacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.LEVEL_CHUNK_PACKET)

	var chunkX: Int
	var chunkZ: Int
	var subChunkCount: Int
	var cacheEnabled: Boolean
	/** @var Int[] */
	 usedBlobHashes = []
	var extraPayload: string

	 static fun withoutCache(chunkX: Int, chunkZ: Int, subChunkCount: Int, payload: string) : self{
		result = new self
		result.chunkX = chunkX
		result.chunkZ = chunkZ
		result.subChunkCount = subChunkCount
		result.extraPayload = payload

		result.cacheEnabled = false

		return result
	}

	/**
	 * @param Int[] usedBlobHashes
	 */
	 static fun withCache(chunkX: Int, chunkZ: Int, subChunkCount: Int, usedBlobHashes: array, extraPayload: string) : self{
		(static fun(Int ...hashes) {})(...usedBlobHashes)
		result = new self
		result.chunkX = chunkX
		result.chunkZ = chunkZ
		result.subChunkCount = subChunkCount
		result.extraPayload = extraPayload

		result.cacheEnabled = true
		result.usedBlobHashes = usedBlobHashes

		return result
	}

	 fun getChunkX() : Int{
		return chunkX
	}

	 fun getChunkZ() : Int{
		return chunkZ
	}

	 fun getSubChunkCount() : Int{
		return subChunkCount
	}

	 fun isCacheEnabled() : Boolean{
		return cacheEnabled
	}

	/**
	 * @return Int[]
	 */
	 fun getUsedBlobHashes() : array{
		return usedBlobHashes
	}

	 fun getExtraPayload() : string{
		return extraPayload
	}

	override fun decodePayload(input: PacketSerializer) {
		chunkX = input.getVarInt()
		chunkZ = input.getVarInt()
		subChunkCount = input.getUnsignedVarInt()
		cacheEnabled = input.getBool()
		if(cacheEnabled){
			for(i =  0, count = input.getUnsignedVarInt() i < count ++i){
				usedBlobHashes[] = input.getLLong()
			}
		}
		extraPayload = input.getString()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putVarInt(chunkX)
		output.putVarInt(chunkZ)
		output.putUnsignedVarInt(subChunkCount)
		output.putBool(cacheEnabled)
		if(cacheEnabled){
			output.putUnsignedVarInt(count(usedBlobHashes))
			foreach(usedBlobHashes hash: as){
				output.putLLong(hash)
			}
		}
		output.putString(extraPayload)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleLevelChunk(this)
	}
}
