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
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.MOVE_PLAYER_PACKET)
class MovePlayerPacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    var entityRuntimeId: Long = -1
    lateinit var position: Vector3
    var pitch: Float = 0F
    var yaw: Float = 0F
    var headYaw: Float = 0F
    var mode: Int = MODE_NORMAL
    var onGround: Boolean = false
    var ridingEid: Long = 0
    var teleportCause: Int = 0
    var teleportItem: Int = 0
    var tick: Long = 0

    override fun decodePayload(input: PacketSerializer) {
        entityRuntimeId = input.getEntityRuntimeId()
        position = input.getVector3()
        pitch = input.getLFloat()
        yaw = input.getLFloat()
        headYaw = input.getLFloat()
        mode = input.getByte()
        onGround = input.getBoolean()
        ridingEid = input.getEntityRuntimeId()
        if (mode == MODE_TELEPORT) {
            teleportCause = input.getLInt()
            teleportItem = input.getLInt()
        }
        tick = input.getUnsignedVarLong()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityRuntimeId(entityRuntimeId)
        output.putVector3(position)
        output.putLFloat(pitch)
        output.putLFloat(yaw)
        output.putLFloat(headYaw)
        output.putByte(mode)
        output.putBoolean(onGround)
        output.putEntityRuntimeId(ridingEid)
        if (mode == MODE_TELEPORT) {
            output.putLInt(teleportCause)
            output.putLInt(teleportItem)
        }
        output.putUnsignedVarLong(tick)
    }

    companion object {
        const val MODE_NORMAL = 0
        const val MODE_RESET = 1
        const val MODE_TELEPORT = 2
        const val MODE_PITCH = 3 // facepalm Mojang
    }
}
