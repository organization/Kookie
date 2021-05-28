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

class ClientCacheBlobStatusPacket : DataPacket(), ServerboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.CLIENT_CACHE_BLOB_STATUS_PACKET)

	/** @var Int[] xxHash64 subchunk data hashes */
	 hitHashes = []
	/** @var Int[] xxHash64 subchunk data hashes */
	 missHashes = []

	/**
	 * @param Int[] hitHashes
	 * @param Int[] missHashes
	 */
	 static fun create(hitHashes: array, missHashes: array) : self{
		//type checks
		(static fun(Int ...hashes) {})(...hitHashes)
		(static fun(Int ...hashes) {})(...missHashes)

		result = new self
		result.hitHashes = hitHashes
		result.missHashes = missHashes
		return result
	}

	/**
	 * @return Int[]
	 */
	 fun getHitHashes() : array{
		return hitHashes
	}

	/**
	 * @return Int[]
	 */
	 fun getMissHashes() : array{
		return missHashes
	}

	override fun decodePayload(input: PacketSerializer) {
		missCount = input.getUnsignedVarInt()
		hitCount = input.getUnsignedVarInt()
		for(i = 0 i < missCount ++i){
			missHashes[] = input.getLLong()
		}
		for(i = 0 i < hitCount ++i){
			hitHashes[] = input.getLLong()
		}
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putUnsignedVarInt(count(missHashes))
		output.putUnsignedVarInt(count(hitHashes))
		foreach(missHashes hash: as){
			output.putLLong(hash)
		}
		foreach(hitHashes hash: as){
			output.putLLong(hash)
		}
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleClientCacheBlobStatus(this)
	}
}
