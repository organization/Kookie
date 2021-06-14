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

@ProtocolIdentify(ProtocolInfo.IDS.RESPAWN_PACKET)
class RespawnPacket : DataPacket(), ClientboundPacket, ServerboundPacket {
    lateinit var position: Vector3
    var respawnState: RespawnStatus = RespawnStatus.SEARCHING_FOR_SPAWN
    var entityRuntimeId: Long = 0

    companion object {
        @JvmStatic
        fun create(position: Vector3, respawnStatus: RespawnStatus, entityRuntimeId: Long): RespawnPacket =
            RespawnPacket().apply {
                this.position = position.asVector3()
                this.respawnState = respawnStatus
                this.entityRuntimeId = entityRuntimeId
            }
    }

    override fun decodePayload(input: PacketSerializer) {
        position = input.getVector3()
        respawnState = RespawnStatus.from(input.getByte())
        entityRuntimeId = input.getEntityRuntimeId()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putVector3(position)
        output.putByte(respawnState.state)
        output.putEntityRuntimeId(entityRuntimeId)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleRespawn(this)
    }

    enum class RespawnStatus(val state: Int) {
        SEARCHING_FOR_SPAWN(0),
        READY_TO_SPAWN(1),
        CLIENT_READY_TO_SPAWN(2);

        companion object {
            private val VALUES = values()
            @JvmStatic
            fun from(value: Int) = VALUES.first { it.state == value }
        }
    }
}
