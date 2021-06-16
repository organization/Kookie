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

@ProtocolIdentify(ProtocolInfo.IDS.MOVE_ACTOR_DELTA_PACKET)
class MoveActorDeltaPacket : DataPacket(), ClientboundPacket {

    var entityRuntimeId: Long = 0
    var flags: Int = 0
    var xPos: Float = 0F
    var yPos: Float = 0F
    var zPos: Float = 0F
    var xRot: Float = 0.0F
    var yRot: Float = 0.0F
    var zRot: Float = 0.0F

    fun maybeReadCoord(flag: Int, input: PacketSerializer): Float {
        if (flags and flag != 0) {
            return input.getLFloat()
        }
        return 0F
    }

    fun maybeReadRotation(flag: Int, input: PacketSerializer): Float {
        if (flags and flag != 0) {
            return input.getByteRotation()
        }
        return 0.0F
    }

    override fun decodePayload(input: PacketSerializer) {
        entityRuntimeId = input.getEntityRuntimeId()
        flags = input.getLShort()
        xPos = maybeReadCoord(FLAG_HAS_X, input)
        yPos = maybeReadCoord(FLAG_HAS_Y, input)
        zPos = maybeReadCoord(FLAG_HAS_Z, input)
        xRot = maybeReadRotation(FLAG_HAS_ROT_X, input)
        yRot = maybeReadRotation(FLAG_HAS_ROT_Y, input)
        zRot = maybeReadRotation(FLAG_HAS_ROT_Z, input)
    }

    fun maybeWriteCoord(flag: Int, value: Float, output: PacketSerializer) {
        if (flags and flag != 0) {
            output.putLFloat(value)
        }
    }

    fun maybeWriteRotation(flag: Int, value: Float, output: PacketSerializer) {
        if (flags and flag != 0) {
            output.putByteRotation(value)
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityRuntimeId(entityRuntimeId)
        output.putLShort(flags)
        maybeWriteCoord(FLAG_HAS_X, xPos, output)
        maybeWriteCoord(FLAG_HAS_Y, yPos, output)
        maybeWriteCoord(FLAG_HAS_Z, zPos, output)
        maybeWriteRotation(FLAG_HAS_ROT_X, xRot, output)
        maybeWriteRotation(FLAG_HAS_ROT_Y, yRot, output)
        maybeWriteRotation(FLAG_HAS_ROT_Z, zRot, output)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleMoveActorDelta(this)

    companion object {
        const val FLAG_HAS_X = 0x01
        const val FLAG_HAS_Y = 0x02
        const val FLAG_HAS_Z = 0x04
        const val FLAG_HAS_ROT_X = 0x08
        const val FLAG_HAS_ROT_Y = 0x10
        const val FLAG_HAS_ROT_Z = 0x20
        const val FLAG_GROUND = 0x40
        const val FLAG_TELEPORT = 0x80
        const val FLAG_FORCE_MOVE_LOCAL_ENTITY = 0x100
    }
}
