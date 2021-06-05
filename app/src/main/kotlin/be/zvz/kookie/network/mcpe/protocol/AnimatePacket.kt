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

@ProtocolIdentify(ProtocolInfo.IDS.ANIMATE_PACKET)
class AnimatePacket : DataPacket(), ClientboundPacket, ServerboundPacket {
    var action: Int = 0
    var entityRuntimeId: Long = 0
    var float: Float = 0f

    override fun decodePayload(input: PacketSerializer) {
        action = input.getVarInt()
        entityRuntimeId = input.getEntityRuntimeId()
        if ((action and 0x80) != 0) {
            float = input.getLFloat()
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putVarInt(action)
        output.putEntityRuntimeId(entityRuntimeId)
        if ((action and 0x80) != 0) {
            output.putLFloat(float)
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleAnimate(this)
    }

    companion object {
        const val ACTION_SWING_ARM = 1

        const val ACTION_STOP_SLEEP = 3
        const val ACTION_CRITICAL_HIT = 4
        const val ACTION_MAGICAL_CRITICAL_HIT = 5
        const val ACTION_ROW_RIGHT = 128
        const val ACTION_ROW_LEFT = 129

        fun create(entityRuntimeId: Long, actionId: Int): AnimatePacket {
            val packet = AnimatePacket().apply {
                this.entityRuntimeId = entityRuntimeId
                this.action = actionId
            }

            return packet
        }

        fun boatHack(entityRuntimeId: Long, actionId: Int, data: Float): AnimatePacket {
            val packet = create(entityRuntimeId, actionId)
            packet.float = data

            return packet
        }
    }
}
