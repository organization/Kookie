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

@ProtocolIdentify(ProtocolInfo.IDS.CONTAINER_SET_DATA_PACKET)
class ContainerSetDataPacket : DataPacket(), ClientboundPacket {

    var windowId: Int = 0
    var property: Int = 0
    var value: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        windowId = input.getByte()
        property = input.getVarInt()
        value = input.getVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(windowId)
        output.putVarInt(property)
        output.putVarInt(value)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleContainerSetData(this)
    }

    enum class PROPERTY(value: Int) {
        FURNACE_SMELT_PROGRESS(0),
        FURNACE_REMAINING_FUEL_TIME(1),
        FURNACE_MAX_FUEL_TIME(2),
        FURNACE_STORED_XP(3),
        FURNACE_FUEL_AUX(4),

        BREWING_STAND_BREW_TIME(0),
        BREWING_STAND_FUEL_AMOUNT(1),
        BREWING_STAND_FUEL_TOTAL(2),
    }

    companion object {
        @JvmStatic
        fun create(windowId: Int, propertyId: Int, value: Int): ContainerSetDataPacket {
            val result = ContainerSetDataPacket()
            result.property = propertyId
            result.value = value
            result.windowId = windowId
            return result
        }
    }
}
