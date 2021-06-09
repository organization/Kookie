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

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.CAMERA_SHAKE_PACKET)
class CameraShakePacket : DataPacket(), ClientboundPacket {
    var intensity: Float = 0f
    var duration: Float = 0f
    var shakeType: Int = 0
    var shakeAction: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        intensity = input.getLFloat()
        duration = input.getLFloat()
        shakeType = input.getByte()
        shakeAction = input.getByte()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putLFloat(intensity)
        output.putLFloat(duration)
        output.putByte(shakeType)
        output.putByte(shakeAction)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleCameraShake(this)
    }

    companion object {
        const val TYPE_POSITIONAL = 0
        const val TYPE_ROTATIONAL = 1

        const val ACTION_ADD = 0
        const val ACTION_STOP = 1

        fun create(intensity: Float, duration: Float, shakeType: Int, shakeAction: Int): CameraShakePacket {

            return CameraShakePacket().apply {
                this.intensity = intensity
                this.duration = duration
                this.shakeType = shakeType
                this.shakeAction = shakeAction
            }
        }
    }
}
