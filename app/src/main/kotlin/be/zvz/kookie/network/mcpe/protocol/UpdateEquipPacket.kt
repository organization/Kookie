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

import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.CacheableNbt

@ProtocolIdentify(ProtocolInfo.IDS.UPDATE_EQUIP_PACKET)
class UpdateEquipPacket : DataPacket(), ClientboundPacket {
    var windowId: Int = 0
    var windowType: Int = 0
    var windowSlotCount: Int = 0 // useless, seems to be part of a standard container header
    var entityUniqueId: Long = 0
    lateinit var namedtag: CacheableNbt

    override fun decodePayload(input: PacketSerializer) {
        windowId = input.getByte()
        windowType = input.getByte()
        windowSlotCount = input.getVarInt()
        entityUniqueId = input.getEntityUniqueId()
        namedtag = CacheableNbt(input.getNbtCompoundRoot())
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(windowId)
        output.putByte(windowType)
        output.putVarInt(windowSlotCount)
        output.putEntityUniqueId(entityUniqueId)
        output.put(namedtag.encodedNbt)
    }
}
