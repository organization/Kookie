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
package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.PLAY_SOUND_PACKET)
class PlaySoundPacket : DataPacket(), ClientboundPacket {

    lateinit var soundName: String
    var x: Float = 0.0f
    var y: Float = 0.0f
    var z: Float = 0.0f
    var volume: Float = 0.0f
    var pitch: Float = 0.0f

    override fun decodePayload(input: PacketSerializer) {
        soundName = input.getString()
        val pos = input.getBlockPosition()
        x = pos.x.toFloat()
        y = pos.y.toFloat()
        z = pos.z.toFloat()
        volume = input.getLFloat()
        pitch = input.getLFloat()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putString(soundName)
        output.putBlockPosition((x * 8).toInt(), (y * 8).toInt(), (z * 8).toInt())
        output.putLFloat(volume)
        output.putLFloat(pitch)
    }
}
