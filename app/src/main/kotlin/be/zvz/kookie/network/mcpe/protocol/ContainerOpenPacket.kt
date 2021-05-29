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

class ContainerOpenPacket : DataPacket(), ClientboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.CONTAINER_OPEN_PACKET)

    var windowId: Int
    var type: Int
    var x: Int
    var y: Int
    var z: Int
    var entityUniqueId: Int = -1

    static
    fun blockInv(windowId: Int, windowType: Int, x: Int, y: Int, z: Int): self {
        result = new self
                result.windowId = windowId
        result.type = windowType
        [result.x, result.y, result.z] = [x, y, z]
        return result
    }

    static
    fun blockInvVec3(windowId: Int, windowType: Int, vector3: Vector3): self {
        return blockInv(windowId, windowType, vector3->getFloorX(), vector3->getFloorY(), vector3->getFloorZ())
    }

    static
    fun entityInv(windowId: Int, windowType: Int, entityUniqueId: Int): self {
        result = new self
                result.windowId = windowId
        result.type = windowType
        result.entityUniqueId = entityUniqueId
        result.x = result.y = result.z = 0 //these have to be set even if they aren't used
        return result
    }

    override fun decodePayload(input: PacketSerializer) {
        windowId = input.getByte()
        type = input.getByte()
        input.getBlockPosition(x, y, z)
        entityUniqueId = input.getEntityUniqueId()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(windowId)
        output.putByte(type)
        output.putBlockPosition(x, y, z)
        output.putEntityUniqueId(entityUniqueId)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleContainerOpen(this)
    }
}
