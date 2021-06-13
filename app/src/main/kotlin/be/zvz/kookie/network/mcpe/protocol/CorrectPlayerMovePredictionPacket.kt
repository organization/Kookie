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

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.CORRECT_PLAYER_MOVE_PREDICTION_PACKET)
class CorrectPlayerMovePredictionPacket : DataPacket(), ClientboundPacket {

    lateinit var position: Vector3
    lateinit var delta: Vector3
    var onGround: Boolean = false
    var tick: Long = 0

    override fun decodePayload(input: PacketSerializer) {
        position = input.getVector3()
        delta = input.getVector3()
        onGround = input.getBoolean()
        tick = input.getUnsignedVarLong()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putVector3(position)
        output.putVector3(delta)
        output.putBoolean(onGround)
        output.putUnsignedVarLong(tick)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleCorrectPlayerMovePrediction(this)
    }

    companion object {
        fun create(
            position: Vector3,
            delta: Vector3,
            onGround: Boolean,
            tick: Long
        ): CorrectPlayerMovePredictionPacket =
            CorrectPlayerMovePredictionPacket().apply {
                this.position = position
                this.delta = delta
                this.onGround = onGround
                this.tick = tick
            }
    }
}
