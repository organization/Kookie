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

class ItemComponentPacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.ITEM_COMPONENT_PACKET)

	/**
	 * @var ItemComponentPacketEntry[]
	 * @phpstan-var list<ItemComponentPacketEntry>
	 */
	 entries

	/**
	 * @param ItemComponentPacketEntry[] entries
	 * @phpstan-param list<ItemComponentPacketEntry> entries
	 */
	 static fun create(entries: array) : self{
		result = new self
		result.entries = entries
		return result
	}

	/**
	 * @return ItemComponentPacketEntry[]
	 * @phpstan-return list<ItemComponentPacketEntry>
	 */
	 fun getEntries() : array{ return entries }

	override fun decodePayload(input: PacketSerializer) {
		entries = []
		for(i = 0, len = input.getUnsignedVarInt() i < len ++i){
			name = input.getString()
			nbt = input.getNbtCompoundRoot()
			entries[] = new ItemComponentPacketEntry(name, nbt)
		}
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putUnsignedVarInt(count(entries))
		foreach(entries entry: as){
			output.putString(entry.getName())
			output.put((new NetworkNbtSerializer())->write(new TreeRoot(entry.getComponentNbt())))
		}
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleItemComponent(this)
	}
}
