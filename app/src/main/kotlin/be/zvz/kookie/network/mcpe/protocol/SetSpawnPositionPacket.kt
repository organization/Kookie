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

@ProtocolIdentify(ProtocolInfo.IDS.SET_SPAWN_POSITION_PACKET)
class SetSpawnPositionPacket : DataPacket(), ClientboundPacket {
    var spawnType: Int = 0

    var pos1: PacketSerializer.BlockPosition = PacketSerializer.BlockPosition()
    var pos2: PacketSerializer.BlockPosition = PacketSerializer.BlockPosition()
    var dimension: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        spawnType = input.getVarInt()
        pos1 = input.getBlockPosition()
        dimension = input.getVarInt()
        pos2 = input.getBlockPosition()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putVarInt(spawnType)
        output.putBlockPosition(pos1)
        output.putVarInt(dimension)
        output.putBlockPosition(pos2)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleSetSpawnPosition(this)

    companion object {
        const val TYPE_PLAYER_SPAWN = 0
        const val TYPE_WORLD_SPAWN = 1

        @JvmStatic
        fun playerSpawn(x: Int, y: Int, z: Int, dimension: Int, x2: Int, y2: Int, z2: Int) =
            SetSpawnPositionPacket().apply {
                this.spawnType = TYPE_PLAYER_SPAWN
                this.pos1 = PacketSerializer.BlockPosition(x, y, z)
                this.pos2 = PacketSerializer.BlockPosition(x2, y2, z2)
                this.dimension = dimension
            }

        @JvmStatic
        fun worldSPawn(x: Int, y: Int, z: Int, dimension: Int) =
            SetSpawnPositionPacket().apply {
                this.spawnType = TYPE_WORLD_SPAWN
                val pos = PacketSerializer.BlockPosition(x, y, z)
                this.pos1 = pos
                this.pos2 = pos
                this.dimension = dimension
            }
    }
}
