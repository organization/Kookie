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
import be.zvz.kookie.network.mcpe.protocol.types.CacheableNbt
import be.zvz.kookie.network.mcpe.protocol.types.inventory.WindowTypes

@ProtocolIdentify(ProtocolInfo.IDS.UPDATE_TRADE_PACKET)
class UpdateTradePacket : DataPacket(), ClientboundPacket {

    // TODO: find fields

    var windowId: Int = 0
    var windowType: WindowTypes = WindowTypes.TRADING // Mojang hardcoded this -_-
    var windowSlotCount: Int = 0 // useless, seems to be part of a standard container header
    var tradeTier: Int = 0
    var traderEid: Long = 0
    var playerEid: Long = 0
    lateinit var displayName: String
    var isV2Trading: Boolean = false
    var isWilling: Boolean = false
    lateinit var offers: CacheableNbt

    override fun decodePayload(input: PacketSerializer) {
        windowId = input.getByte()
        windowType = WindowTypes.from(input.getByte())
        windowSlotCount = input.getVarInt()
        tradeTier = input.getVarInt()
        traderEid = input.getEntityUniqueId()
        playerEid = input.getEntityUniqueId()
        displayName = input.getString()
        isV2Trading = input.getBoolean()
        isWilling = input.getBoolean()
        offers = CacheableNbt(input.getNbtCompoundRoot())
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(windowId)
        output.putByte(windowType.value)
        output.putVarInt(windowSlotCount)
        output.putVarInt(tradeTier)
        output.putEntityUniqueId(traderEid)
        output.putEntityUniqueId(playerEid)
        output.putString(displayName)
        output.putBoolean(isV2Trading)
        output.putBoolean(isWilling)
        output.put(offers.encodedNbt)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleUpdateTrade(this)
    }
}
