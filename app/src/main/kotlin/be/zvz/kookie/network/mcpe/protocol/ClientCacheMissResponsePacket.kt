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

class ClientCacheMissResponsePacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.CLIENT_CACHE_MISS_RESPONSE_PACKET)

	/** @var ChunkCacheBlob[] */
	 blobs = []

	/**
	 * @param ChunkCacheBlob[] blobs
	 */
	 static fun create(blobs: array) : self{
		//type check
		(static fun(ChunkCacheBlob ...blobs) {})(...blobs)

		result = new self
		result.blobs = blobs
		return result
	}

	/**
	 * @return ChunkCacheBlob[]
	 */
	 fun getBlobs() : array{
		return blobs
	}

	override fun decodePayload(input: PacketSerializer) {
		for(i = 0, count = input.getUnsignedVarInt() i < count ++i){
			hash = input.getLLong()
			payload = input.getString()
			blobs[] = new ChunkCacheBlob(hash, payload)
		}
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putUnsignedVarInt(count(blobs))
		foreach(blobs blob: as){
			output.putLLong(blob.getHash())
			output.putString(blob.getPayload())
		}
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleClientCacheMissResponse(this)
	}
}
