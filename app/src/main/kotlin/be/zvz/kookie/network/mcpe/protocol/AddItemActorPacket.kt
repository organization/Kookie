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
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.entity.MetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.inventory.ItemStackWrapper
import com.koloboke.collect.map.hash.HashIntObjMaps

@ProtocolIdentify(ProtocolInfo.IDS.ADD_ITEM_ACTOR_PACKET)
class AddItemActorPacket : DataPacket(), ClientboundPacket {

    var entityUniqueId: Long? = null
    var entityRuntimeId: Long = -1
    lateinit var item: ItemStackWrapper
    lateinit var position: Vector3
    var motion: Vector3? = null

    var metadata: MutableMap<Int, MetadataProperty> = HashIntObjMaps.newMutableMap()

    var isFromFishing: Boolean = false

    override fun decodePayload(input: PacketSerializer) {
        entityUniqueId = input.getEntityUniqueId()
        entityRuntimeId = input.getEntityRuntimeId()
        item = ItemStackWrapper.read(input)
        position = input.getVector3()
        motion = input.getVector3()
        metadata = input.getEntityMetadataProperty()
        isFromFishing = input.getBoolean()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityUniqueId(entityUniqueId ?: entityRuntimeId)
        output.putEntityRuntimeId(entityRuntimeId)
        item.write(output)
        output.putVector3(position)
        output.putVector3(motion ?: Vector3())
        output.putEntityMetadata(metadata)
        output.putBoolean(isFromFishing)
    }
}
