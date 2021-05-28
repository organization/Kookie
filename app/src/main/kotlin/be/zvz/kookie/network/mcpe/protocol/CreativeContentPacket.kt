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

class CreativeContentPacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.CREATIVE_CONTENT_PACKET)

	/** @var CreativeContentEntry[] */
	 entries

	/**
	 * @param CreativeContentEntry[] entries
	 */
	 static fun create(entries: array) : self{
		result = new self
		result.entries = entries
		return result
	}

	/** @return CreativeContentEntry[] */
	 fun getEntries() : array{ return entries }

	override fun decodePayload(input: PacketSerializer) {
		entries = []
		for(i = 0, len = input.getUnsignedVarInt() i < len ++i){
			entries[] = CreativeContentEntry::read(input)
		}
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putUnsignedVarInt(count(entries))
		foreach(entries entry: as){
			entry.write(output)
		}
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleCreativeContent(this)
	}
}
