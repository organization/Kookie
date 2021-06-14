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

@ProtocolIdentify(ProtocolInfo.IDS.MOVE_ACTOR_ABSOLUTE_PACKET)
class MoveActorAbsolutePacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    var entityRuntimeId: Long = -1
    var flags: Int = 0
    lateinit var position: Vector3
    var xRot: Float = 0F
    var yRot: Float = 0F
    var zRot: Float = 0F

    override fun decodePayload(input: PacketSerializer) {
        entityRuntimeId = input.getEntityRuntimeId()
        flags = input.getByte()
        position = input.getVector3()
        xRot = input.getByteRotation()
        yRot = input.getByteRotation()
        zRot = input.getByteRotation()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityRuntimeId(entityRuntimeId)
        output.putByte(flags)
        output.putVector3(position)
        output.putByteRotation(xRot)
        output.putByteRotation(yRot)
        output.putByteRotation(zRot)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleMoveActorAbsolute(this)
    }

    companion object {
        const val FLAG_GROUND = 0x01
        const val FLAG_TELEPORT = 0x02
        const val UNKNOWN = 0x03
        const val FLAG_FORCE_MOVE_LOCAL_ENTITY = 0x04

        @JvmStatic
        @JvmOverloads
        fun create(
            entityRuntimeId: Long,
            pos: Vector3,
            xRot: Float,
            yRot: Float,
            zRot: Float,
            flags: Int = 0
        ): MoveActorAbsolutePacket = MoveActorAbsolutePacket().apply {
            this.entityRuntimeId = entityRuntimeId
            this.flags = flags
            this.position = pos
            this.xRot = xRot
            this.yRot = yRot
            this.zRot = zRot
        }
    }
}
