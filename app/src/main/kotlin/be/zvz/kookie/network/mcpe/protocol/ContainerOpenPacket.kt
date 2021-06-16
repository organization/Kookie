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

@ProtocolIdentify(ProtocolInfo.IDS.CONTAINER_OPEN_PACKET)
class ContainerOpenPacket : DataPacket(), ClientboundPacket {

    var windowId: Int = 0
    var type: Int = 0
    var pos = PacketSerializer.BlockPosition()
    var entityUniqueId: Long = -1

    override fun decodePayload(input: PacketSerializer) {
        windowId = input.getByte()
        type = input.getByte()
        input.getBlockPosition(pos)
        entityUniqueId = input.getEntityUniqueId()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(windowId)
        output.putByte(type)
        output.putBlockPosition(pos)
        output.putEntityUniqueId(entityUniqueId)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleContainerOpen(this)

    companion object {
        @JvmStatic
        fun blockInv(
            windowId: Int,
            windowType: Int,
            x: Int,
            y: Int,
            z: Int
        ) =
            ContainerOpenPacket().apply {
                this.windowId = windowId
                this.type = windowType
                this.pos = PacketSerializer.BlockPosition(x, y, z)
            }

        @JvmStatic
        fun blockInvVec3(
            windowId: Int,
            windowType: Int,
            vector3: Vector3
        ): ContainerOpenPacket =
            blockInv(windowId, windowType, vector3.x.toInt(), vector3.y.toInt(), vector3.z.toInt())

        @JvmStatic
        fun entityInv(
            windowId: Int,
            windowType: Int,
            entityUniqueId: Long
        ) =
            ContainerOpenPacket().apply {
                this.windowId = windowId
                this.type = windowType
                this.entityUniqueId = entityUniqueId
                this.pos = PacketSerializer.BlockPosition()
            }
    }
}
