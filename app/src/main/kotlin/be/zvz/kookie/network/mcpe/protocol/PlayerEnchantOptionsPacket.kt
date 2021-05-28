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

class PlayerEnchantOptionsPacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.PLAYER_ENCHANT_OPTIONS_PACKET)

	/** @var EnchantOption[] */
	 options

	/**
	 * @param EnchantOption[] options
	 */
	 static fun create(options: array) : self{
		result = new self
		result.options = options
		return result
	}

	/**
	 * @return EnchantOption[]
	 */
	 fun getOptions() : array{ return options }

	override fun decodePayload(input: PacketSerializer) {
		options = []
		for(i = 0, len = input.getUnsignedVarInt() i < len ++i){
			options[] = EnchantOption::read(input)
		}
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putUnsignedVarInt(count(options))
		foreach(options option: as){
			option.write(output)
		}
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handlePlayerEnchantOptions(this)
	}
}
