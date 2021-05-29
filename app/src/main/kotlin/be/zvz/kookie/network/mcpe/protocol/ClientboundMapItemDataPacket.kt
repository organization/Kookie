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
import be.zvz.kookie.network.mcpe.protocol.types.DimensionIds
import be.zvz.kookie.network.mcpe.protocol.types.MapTrackedObject
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer
import be.zvz.kookie.utils.Binary
import java.util.concurrent.atomic.AtomicInteger

@ProtocolIdentify(ProtocolInfo.IDS.CLIENTBOUND_MAP_ITEM_DATA_PACKET)
class ClientboundMapItemDataPacket : DataPacket(), ClientboundPacket {
    var mapId: Long = 0
    var type: Int = 0
    var dimensionId: DimensionIds = DimensionIds.OVERWORLD
    var isLocked: Boolean = false

    /** @var Int[] */
    var eids: MutableList<Long> = mutableListOf()
    var scale: Int = 0

    val trackedEntities: MutableList<MapTrackedObject> = mutableListOf()
    /** @var MapDecoration[] */
    decorations = []

    var width: Int
    var height: Int
    var xOffset: Int = 0
    var yOffset: Int = 0
    /** @var Color[][] */
    colors = []

    override fun decodePayload(input: PacketSerializer) {
        mapId = input.getEntityUniqueId()
        type = input.getUnsignedVarInt()
        dimensionId = DimensionIds.fromInt(input.getByte())
        isLocked = input.getBoolean()

        if ((type and 0x08) != 0) {
            for (i in 0..input.getUnsignedVarInt()) {
                eids[i] = input.getEntityUniqueId()
            }
        }

        if ((type and (0x08 or BITFLAG_DECORATION_UPDATE or BITFLAG_TEXTURE_UPDATE)) != 0) {
            scale = input.getByte()
        }

        if ((type and BITFLAG_DECORATION_UPDATE) != 0) {
            for (i in 0..input.getUnsignedVarInt()) {
                val obj = MapTrackedObject()
                obj.type = input.getLInt()

                when (obj.type) {
                    MapTrackedObject.TYPE_BLOCK -> {
                        val x = AtomicInteger()
                        val y = AtomicInteger()
                        val z = AtomicInteger()
                        input.getBlockPosition(x, y, z)

                        obj.x = x.get()
                        obj.y = y.get()
                        obj.z = z.get()
                    }
                    MapTrackedObject.TYPE_ENTITY -> obj.entityUniqueId = input.getEntityUniqueId()
                    else -> PacketDecodeException("Unknown map object type object.type")
                }

                trackedEntities.add(obj)
            }
            for (i in 0..input.getUnsignedVarInt()) {
                val icon = input.getByte()
                val rotation = input.getByte()
                val xOffset = input.getByte()
                val yOffset = input.getByte()
                val label = input.getString()
                val color = Color.fromRGBA(Binary.flipIntEndianness(input.getUnsignedVarInt()))
                decorations = MapDecoration(icon, rotation, xOffset, yOffset, label, color)
            }
        }

        if ((type & BITFLAG_TEXTURE_UPDATE) !== 0){
            width = input.getVarInt()
            height = input.getVarInt()
            xOffset = input.getVarInt()
            yOffset = input.getVarInt()

            count = input.getUnsignedVarInt()
            if (count !== width * height) {
                throw new PacketDecodeException ("Expected colour count of ".(height * width)." (height height * width width), got count")
            }

            for (y = 0 y < height ++y){
            for (x = 0 x < width ++x){
            colors[y][x] = Color::fromRGBA(Binary::flipIntEndianness(input.getUnsignedVarInt()))
        }
        }
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityUniqueId(mapId)

        type = 0
        if ((eidsCount = count(eids)) > 0) {
            type | = 0x08
        }
        if ((decorationCount = count(decorations)) > 0) {
            type | = BITFLAG_DECORATION_UPDATE
        }
        if (count(colors) > 0) {
            type | = BITFLAG_TEXTURE_UPDATE
        }

        output.putUnsignedVarInt(type)
        output.putByte(dimensionId)
        output.putBool(isLocked)

        if ((type & 0x08) !== 0){ //TODO: find out what these are for
            output.putUnsignedVarInt(eidsCount)
            foreach(eids eid : as) {
                output.putEntityUniqueId(eid)
            }
        }

        if ((type & (0x08 | BITFLAG_TEXTURE_UPDATE | BITFLAG_DECORATION_UPDATE)) !== 0){
            output.putByte(scale)
        }

        if ((type & BITFLAG_DECORATION_UPDATE) !== 0){
            output.putUnsignedVarInt(count(trackedEntities))
            foreach(trackedEntities object : as) {
                output.putLInt(object.type)
                if (object.type === MapTrackedObject::TYPE_BLOCK) {
                    output.putBlockPosition(object.x, object.y, object.z)
                } elseif (object.type === MapTrackedObject::TYPE_ENTITY){
                    output.putEntityUniqueId(object.entityUniqueId)
                }else{
                throw new \InvalidArgumentException("Unknown map object type object.type")
            }
            }

            output.putUnsignedVarInt(decorationCount)
            foreach(decorations decoration : as) {
                output.putByte(decoration.getIcon())
                output.putByte(decoration.getRotation())
                output.putByte(decoration.getXOffset())
                output.putByte(decoration.getYOffset())
                output.putString(decoration.getLabel())
                output.putUnsignedVarInt(Binary::flipIntEndianness(decoration.getColor()->toRGBA()))
            }
        }

        if ((type & BITFLAG_TEXTURE_UPDATE) !== 0){
            output.putVarInt(width)
            output.putVarInt(height)
            output.putVarInt(xOffset)
            output.putVarInt(yOffset)

            output.putUnsignedVarInt(width * height) //list count, but we handle it as a 2D array... thanks for the confusion mojang

            for (y = 0 y < height ++y){
            for (x = 0 x < width ++x){
            //if mojang had any sense this would just be a regular LE Int
            output.putUnsignedVarInt(Binary::flipIntEndianness(colors[y][x]->toRGBA()))
        }
        }
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleClientboundMapItemData(this)
    }

    companion object {
        const val BITFLAG_TEXTURE_UPDATE = 0x02
        const val BITFLAG_DECORATION_UPDATE = 0x04
    }
}
