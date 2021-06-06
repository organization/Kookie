package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

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

@ProtocolIdentify(ProtocolInfo.IDS.MOTION_PREDICTION_HINTS_PACKET)
class MotionPredictionHintsPacket : DataPacket(), ClientboundPacket {

    var entityRuntimeId: Long = 0
    lateinit var motion: Vector3
    var onGround: Boolean = false

    fun isOnGround(): Boolean {
        return onGround
    }

    override fun decodePayload(input: PacketSerializer) {
        entityRuntimeId = input.getEntityRuntimeId()
        motion = input.getVector3()
        onGround = input.getBoolean()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityRuntimeId(entityRuntimeId)
        output.putVector3(motion)
        output.putBoolean(onGround)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleMotionPredictionHints(this)
    }

    companion object {
        fun create(entityRuntimeId: Long, motion: Vector3, onGround: Boolean): MotionPredictionHintsPacket =
            MotionPredictionHintsPacket().apply {
                this.entityRuntimeId = entityRuntimeId
                this.motion = motion
                this.onGround = onGround
            }
    }
}
