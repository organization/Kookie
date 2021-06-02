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
import java.util.concurrent.atomic.AtomicInteger

@ProtocolIdentify(ProtocolInfo.IDS.PLAYER_ACTION_PACKET)
class PlayerActionPacket : DataPacket(), ServerboundPacket {
    var entityRuntimeId: Long = 0
    var action: Int = 0
    var x: AtomicInteger = AtomicInteger()
    var y: AtomicInteger = AtomicInteger()
    var z: AtomicInteger = AtomicInteger()
    var face: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        this.entityRuntimeId = input.getEntityRuntimeId()
        this.action = input.getVarInt()
        input.getBlockPosition(x, y, z)
        this.face = input.getVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityRuntimeId(entityRuntimeId)
        output.putVarInt(action)
        output.putBlockPosition(x.get(), y.get(), z.get())
        output.putVarInt(face)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handlePlayerAction(this)
    }

    companion object {
        const val ACTION_START_BREAK = 0
        const val ACTION_ABORT_BREAK = 1
        const val ACTION_STOP_BREAK = 2
        const val ACTION_GET_UPDATED_BLOCK = 3
        const val ACTION_DROP_ITEM = 4
        const val ACTION_START_SLEEPING = 5
        const val ACTION_STOP_SLEEPING = 6
        const val ACTION_RESPAWN = 7
        const val ACTION_JUMP = 8
        const val ACTION_START_SPRInt = 9
        const val ACTION_STOP_SPRInt = 10
        const val ACTION_START_SNEAK = 11
        const val ACTION_STOP_SNEAK = 12
        const val ACTION_CREATIVE_PLAYER_DESTROY_BLOCK = 13
        const val ACTION_DIMENSION_CHANGE_ACK = 14
        const val ACTION_START_GLIDE = 15
        const val ACTION_STOP_GLIDE = 16
        const val ACTION_BUILD_DENIED = 17
        const val ACTION_CRACK_BREAK = 18
        const val ACTION_CHANGE_SKIN = 19
        const val ACTION_SET_ENCHANTMENT_SEED = 20
        const val ACTION_START_SWIMMING = 21
        const val ACTION_STOP_SWIMMING = 22
        const val ACTION_START_SPIN_ATTACK = 23
        const val ACTION_STOP_SPIN_ATTACK = 24
        const val ACTION_INTERACT_BLOCK = 25
        const val ACTION_PREDICT_DESTROY_BLOCK = 26
        const val ACTION_CONTINUE_DESTROY_BLOCK = 27
    }
}
