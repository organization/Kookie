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
import be.zvz.kookie.network.mcpe.protocol.types.skin.SkinData
import java.util.UUID

@ProtocolIdentify(ProtocolInfo.IDS.PLAYER_SKIN_PACKET)
class PlayerSkinPacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    lateinit var uuid: UUID
    lateinit var oldSkinName: String
    lateinit var newSkinName: String
    lateinit var skin: SkinData

    override fun decodePayload(input: PacketSerializer) {
        uuid = input.getUUID()
        skin = input.getSkin()
        newSkinName = input.getString()
        oldSkinName = input.getString()
        skin.isVerified = input.getBoolean()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putUUID(uuid)
        output.putSkin(skin)
        output.putString(newSkinName)
        output.putString(oldSkinName)
        output.putBoolean(skin.isVerified)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handlePlayerSkin(this)

    companion object {
        @JvmStatic
        fun create(uuid: UUID, skinData: SkinData) = PlayerSkinPacket().apply {
            this.uuid = uuid
            this.skin = skinData
        }
    }
}
