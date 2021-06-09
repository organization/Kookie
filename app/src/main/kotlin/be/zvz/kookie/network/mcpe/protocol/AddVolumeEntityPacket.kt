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

import be.zvz.kookie.nbt.TreeRoot
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.NetworkNbtSerializer
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.ADD_VOLUME_ENTITY_PACKET)
class AddVolumeEntityPacket : DataPacket(), ClientboundPacket {

    var entityNetId: Int = 0

    lateinit var data: CompoundTag

    override fun decodePayload(input: PacketSerializer) {
        entityNetId = input.getUnsignedVarInt()
        data = input.getNbtCompoundRoot()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putUnsignedVarInt(entityNetId)
        output.put(NetworkNbtSerializer().write(TreeRoot(data)))
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleAddVolumeEntity(this)
    }
}
