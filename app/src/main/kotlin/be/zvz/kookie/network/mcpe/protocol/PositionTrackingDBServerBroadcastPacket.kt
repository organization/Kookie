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
import be.zvz.kookie.network.mcpe.protocol.types.CacheableNbt

@ProtocolIdentify(ProtocolInfo.IDS.POSITION_TRACKING_D_B_SERVER_BROADCAST_PACKET)
class PositionTrackingDBServerBroadcastPacket : DataPacket(), ClientboundPacket {
    var action: Action = Action.UPDATE
    var trackingId: Int = 0
    lateinit var nbt: CacheableNbt

    fun create(action: Action, trackingId: Int, nbt: CacheableNbt): PositionTrackingDBServerBroadcastPacket =
        PositionTrackingDBServerBroadcastPacket().apply {
            this.action = action
            this.trackingId = trackingId
            this.nbt = nbt
        }

    override fun decodePayload(input: PacketSerializer) {
        action = Action.from(input.getByte())
        trackingId = input.getVarInt()
        nbt = CacheableNbt(input.getNbtCompoundRoot())
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(action.value)
        output.putVarInt(trackingId)
        output.put(nbt.encodedNbt)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handlePositionTrackingDBServerBroadcast(this)

    enum class Action(val value: Int) {
        UPDATE(0),
        DESTROY(1),
        NOT_FOUND(2);

        companion object {
            private val VALUES = values()
            @JvmStatic
            fun from(value: Int) = VALUES.first { it.value == value }
        }
    }
}
