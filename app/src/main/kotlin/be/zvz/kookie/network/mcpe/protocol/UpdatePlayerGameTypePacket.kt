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
import be.zvz.kookie.player.GameMode

@ProtocolIdentify(ProtocolInfo.IDS.UPDATE_PLAYER_GAME_TYPE_PACKET)
class UpdatePlayerGameTypePacket : DataPacket(), ClientboundPacket {
    var gameMode: GameMode = GameMode.SURVIVAL
    var playerEntityUniqueId: Long = 0

    override fun decodePayload(input: PacketSerializer) {
        gameMode = GameMode.from(input.getVarInt())
        playerEntityUniqueId = input.getEntityUniqueId()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putVarInt(gameMode.id())
        output.putEntityUniqueId(playerEntityUniqueId)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleUpdatePlayerGameType(this)
    }

    companion object {
        fun create(gameMode: GameMode, playerEntityUniqueId: Long): UpdatePlayerGameTypePacket =
            UpdatePlayerGameTypePacket().apply {
                this.gameMode = gameMode
                this.playerEntityUniqueId = playerEntityUniqueId
            }
    }
}
