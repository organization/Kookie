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

class StructureBlockUpdatePacket : DataPacket(), ServerboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.STRUCTURE_BLOCK_UPDATE_PACKET)

	var x: Int
	var y: Int
	var z: Int
	var structureEditorData: StructureEditorData
	var isPowered: Boolean

	override fun decodePayload(input: PacketSerializer) {
		input.getBlockPosition(x, y, z)
		structureEditorData = input.getStructureEditorData()
		isPowered = input.getBool()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putBlockPosition(x, y, z)
		output.putStructureEditorData(structureEditorData)
		output.putBool(isPowered)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleStructureBlockUpdate(this)
	}
}
