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

@ProtocolIdentify(ProtocolInfo.IDS.UPDATE_BLOCK_SYNCED_PACKET)
class UpdateBlockSyncedPacket : UpdateBlockPacket() {

    var entityUniqueId: Long = 0
    var updateType: Long = 0

    override fun decodePayload(input: PacketSerializer) {
        super.decodePayload(input)
        entityUniqueId = input.getUnsignedVarLong()
        updateType = input.getUnsignedVarLong()
    }

    override fun encodePayload(output: PacketSerializer) {
        super.encodePayload(output)
        output.putUnsignedVarLong(entityUniqueId)
        output.putUnsignedVarLong(updateType)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleUpdateBlockSynced(this)

    companion object {
        const val NONE = 0
        const val CREATE = 1
        const val DESTROY = 2
    }
}
