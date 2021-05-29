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
use Ramsey\Uuid\UuidInterface

class PlayerSkinPacket : DataPacket(), ClientboundPacket, ServerboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.PLAYER_SKIN_PACKET)

    var uuid: UuidInterface
    var oldSkinName: string = ""
    var newSkinName: string = ""
    var skin: SkinData

    static
    fun create(uuid: UuidInterface, skinData: SkinData): self {
        result = new self
                result.uuid = uuid
        result.skin = skinData
        return result
    }

    override fun decodePayload(input: PacketSerializer) {
        uuid = input.getUUID()
        skin = input.getSkin()
        newSkinName = input.getString()
        oldSkinName = input.getString()
        skin->setVerified(input.getBool())
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putUUID(uuid)
        output.putSkin(skin)
        output.putString(newSkinName)
        output.putString(oldSkinName)
        output.putBool(skin->isVerified())
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handlePlayerSkin(this)
    }
}
