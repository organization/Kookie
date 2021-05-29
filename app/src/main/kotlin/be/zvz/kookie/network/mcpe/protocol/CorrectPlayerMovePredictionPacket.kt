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

class CorrectPlayerMovePredictionPacket : DataPacket(), ClientboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.CORRECT_PLAYER_MOVE_PREDICTION_PACKET)

    var position: Vector3
    var delta: Vector3
    var onGround: Boolean
    var tick: Int

    static
    fun create(position: Vector3, delta: Vector3, onGround: Boolean, tick: Int): self {
        result = new self
                result.position = position
        result.delta = delta
        result.onGround = onGround
        result.tick = tick
        return result
    }

    fun getPosition(): Vector3 {
        return position
    }

    fun getDelta(): Vector3 {
        return delta
    }

    fun isOnGround(): Boolean {
        return onGround
    }

    fun getTick(): Int {
        return tick
    }

    override fun decodePayload(input: PacketSerializer) {
        position = input.getVector3()
        delta = input.getVector3()
        onGround = input.getBool()
        tick = input.getUnsignedVarLong()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putVector3(position)
        output.putVector3(delta)
        output.putBool(onGround)
        output.putUnsignedVarLong(tick)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleCorrectPlayerMovePrediction(this)
    }
}
