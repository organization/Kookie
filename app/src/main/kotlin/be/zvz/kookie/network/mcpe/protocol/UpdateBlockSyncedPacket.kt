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

class UpdateBlockSyncedPacket : UpdateBlockPacket() {
    @ProtocolIdentify(ProtocolInfo.IDS.UPDATE_BLOCK_SYNCED_PACKET)

    const val TYPE_NONE = 0
    const val TYPE_CREATE = 1
    const val TYPE_DESTROY = 2

    var entityUniqueId: Int
    var updateType: Int

    override fun decodePayload(input: PacketSerializer) {
        parent::decodePayload(input)
        entityUniqueId = input.getUnsignedVarLong()
        updateType = input.getUnsignedVarLong()
    }

    override fun encodePayload(output: PacketSerializer) {
        parent::encodePayload(output)
        output.putUnsignedVarLong(entityUniqueId)
        output.putUnsignedVarLong(updateType)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleUpdateBlockSynced(this)
    }
}
