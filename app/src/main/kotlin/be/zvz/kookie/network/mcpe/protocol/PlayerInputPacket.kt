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

@ProtocolIdentify(ProtocolInfo.IDS.PLAYER_INPUT_PACKET)
class PlayerInputPacket : DataPacket(), ServerboundPacket {

    var motionX: Float = 0.0f
    var motionY: Float = 0.0f
    var jumping: Boolean = false
    var sneaking: Boolean = false

    override fun decodePayload(input: PacketSerializer) {
        motionX = input.getLFloat()
        motionY = input.getLFloat()
        jumping = input.getBoolean()
        sneaking = input.getBoolean()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putLFloat(motionX)
        output.putLFloat(motionY)
        output.putBoolean(jumping)
        output.putBoolean(sneaking)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handlePlayerInput(this)
}
