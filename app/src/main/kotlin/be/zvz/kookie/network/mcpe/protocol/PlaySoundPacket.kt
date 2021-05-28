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

class PlaySoundPacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.PLAY_SOUND_PACKET)

	var soundName: string
	var x: Float
	var y: Float
	var z: Float
	var volume: Float
	var pitch: Float

	override fun decodePayload(input: PacketSerializer) {
		soundName = input.getString()
		input.getBlockPosition(x, y, z)
		x /= 8
		y /= 8
		z /= 8
		volume = input.getLFloat()
		pitch = input.getLFloat()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putString(soundName)
		output.putBlockPosition((Int) (x * 8), (Int) (y * 8), (Int) (z * 8))
		output.putLFloat(volume)
		output.putLFloat(pitch)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handlePlaySound(this)
	}
}
