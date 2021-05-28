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
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.ANIMATE_ENTITY_PACKET)
class AnimateEntityPacket : DataPacket(), ClientboundPacket {
    lateinit var animation: String
    lateinit var nextState: String
    lateinit var stopExpression: String
    lateinit var controller: String
    var blendOutTime: Float = 0f

    var actorRuntimeIds: MutableList<Long> = mutableListOf()

    companion object {

        fun create(
            animation: String,
            nextState: String,
            stopExpression: String,
            controller: String,
            blendOutTime: Float,
            actorRuntimeIds: MutableList<Long>,
        ): AnimateEntityPacket {
            val packet = AnimateEntityPacket().apply {
                this.animation = animation
                this.nextState = nextState
                this.stopExpression = stopExpression
                this.controller = controller
                this.blendOutTime = blendOutTime
                this.actorRuntimeIds = actorRuntimeIds
            }

            return packet
        }
    }

    override fun decodePayload(input: PacketSerializer) {
        animation = input.getString()
        nextState = input.getString()
        stopExpression = input.getString()
        controller = input.getString()
        blendOutTime = input.getLFloat()
        actorRuntimeIds = mutableListOf()

        for (i in 0..input.getUnsignedVarInt()) {
            actorRuntimeIds[i] = input.getEntityRuntimeId()
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putString(animation)
        output.putString(nextState)
        output.putString(stopExpression)
        output.putString(controller)
        output.putLFloat(blendOutTime)
        output.putUnsignedVarInt(actorRuntimeIds.size)

        for (id in actorRuntimeIds) {
            output.putEntityRuntimeId(id)
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleAnimateEntity(this)
    }
}
