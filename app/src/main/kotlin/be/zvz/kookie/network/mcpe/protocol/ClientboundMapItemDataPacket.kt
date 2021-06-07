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

import be.zvz.kookie.color.Color
import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.DimensionIds
import be.zvz.kookie.network.mcpe.protocol.types.MapDecoration
import be.zvz.kookie.network.mcpe.protocol.types.MapTrackedObject
import be.zvz.kookie.utils.Binary
import com.koloboke.collect.map.hash.HashIntObjMaps

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
    var decorations: MutableList<MapDecoration> = mutableListOf()

    var width: Int = 0
    var height: Int = 0
    var xOffset: Int = 0
    var yOffset: Int = 0

    /** @var Color[][] */
    var colors: MutableMap<Int, MutableMap<Int, Color>> = HashIntObjMaps.newMutableMap()

    override fun decodePayload(input: PacketSerializer) {
        mapId = input.getEntityUniqueId()
        type = input.getUnsignedVarInt()
        dimensionId = DimensionIds.fromInt(input.getByte())
        isLocked = input.getBoolean()

        if (type and 0x08 != 0) {
            for (i in 0 until input.getUnsignedVarInt()) {
                eids[i] = input.getEntityUniqueId()
            }
        }

        if (type and (0x08 or BITFLAG_DECORATION_UPDATE or BITFLAG_TEXTURE_UPDATE) != 0) {
            scale = input.getByte()
        }

        if (type and BITFLAG_DECORATION_UPDATE != 0) {
            for (i in 0 until input.getUnsignedVarInt()) {
                val obj = MapTrackedObject()
                obj.type = input.getLInt()

                when (obj.type) {
                    MapTrackedObject.TYPE_BLOCK -> {
                        val pos = input.getBlockPosition()
                        obj.x = pos.x
                        obj.y = pos.x
                        obj.z = pos.x
                    }
                    MapTrackedObject.TYPE_ENTITY -> obj.entityUniqueId = input.getEntityUniqueId()
                    else -> throw PacketDecodeException("Unknown map object type ${obj.type}")
                }

                trackedEntities.add(obj)
            }
            for (i in 0 until input.getUnsignedVarInt()) {
                val icon = input.getByte()
                val rotation = input.getByte()
                val xOffset = input.getByte()
                val yOffset = input.getByte()
                val label = input.getString()
                val color = Color.fromRGBA(Binary.flipIntEndianness(input.getUnsignedVarInt()))
                decorations.add(MapDecoration(icon, rotation, xOffset, yOffset, label, color))
            }
        }

        if (type and BITFLAG_TEXTURE_UPDATE != 0) {
            width = input.getVarInt()
            height = input.getVarInt()
            xOffset = input.getVarInt()
            yOffset = input.getVarInt()

            val count = input.getUnsignedVarInt()
            if (count != width * height) {
                throw PacketDecodeException("Expected colour count of ${height * width} (height height * width width), got count")
            }

            for (y in 0 until height) {
                for (x in 0 until width) {
                    colors.getValue(y)[x] = Color.fromRGBA(Binary.flipIntEndianness(input.getUnsignedVarInt()))
                }
            }
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityUniqueId(mapId)

        type = 0
        val eidsCount = eids.size
        if (eidsCount > 0) {
            type = type or 0x08
        }
        val decorationCount = decorations.size
        if (decorationCount > 0) {
            type = type or BITFLAG_DECORATION_UPDATE
        }
        val colorCount = colors.size
        if (colorCount > 0) {
            type = type or BITFLAG_TEXTURE_UPDATE
        }

        output.putUnsignedVarInt(type)
        output.putByte(dimensionId.id)
        output.putBoolean(isLocked)

        if (type and 0x08 != 0) { // TODO: find out what these are for
            output.putUnsignedVarInt(eidsCount)
            eids.forEach {
                output.putEntityUniqueId(it)
            }
        }

        if (type and (0x08 or BITFLAG_TEXTURE_UPDATE or BITFLAG_DECORATION_UPDATE) != 0) {
            output.putByte(scale)
        }

        if (type and BITFLAG_DECORATION_UPDATE != 0) {
            output.putUnsignedVarInt(trackedEntities.size)
            trackedEntities.forEach {
                output.putLInt(it.type)
                when (it.type) {
                    MapTrackedObject.TYPE_BLOCK -> {
                        output.putBlockPosition(it.x, it.y, it.z)
                    }
                    MapTrackedObject.TYPE_ENTITY -> {
                        output.putEntityUniqueId(it.entityUniqueId)
                    }
                    else -> throw MapTrackedObjectException("Unknown map object type ${it.type}")
                }
            }

            output.putUnsignedVarInt(decorationCount)
            decorations.forEach { decoration ->
                output.putByte(decoration.icon)
                output.putByte(decoration.rotation)
                output.putByte(decoration.xOffset)
                output.putByte(decoration.yOffset)
                output.putString(decoration.label)
                output.putUnsignedVarInt(Binary.flipIntEndianness(decoration.color.toRGBA()))
            }
        }

        if (type and BITFLAG_TEXTURE_UPDATE != 0) {
            output.putVarInt(width)
            output.putVarInt(height)
            output.putVarInt(xOffset)
            output.putVarInt(yOffset)

            output.putUnsignedVarInt(width * height) // list count, but we handle it as a 2D array... thanks for the confusion mojang

            for (y in 0 until height) {
                for (x in 0 until width) {
                    // if mojang had any sense this would just be a regular LE Int
                    output.putUnsignedVarInt(Binary.flipIntEndianness(colors.getValue(y).getValue(x).toRGBA()))
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

    class MapTrackedObjectException(override val message: String) : RuntimeException()
}
