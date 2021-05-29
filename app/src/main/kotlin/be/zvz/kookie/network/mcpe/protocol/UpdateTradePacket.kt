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

class UpdateTradePacket : DataPacket(), ClientboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.UPDATE_TRADE_PACKET)

    //TODO: find fields

    var windowId: Int
    var windowType: Int = WindowTypes::TRADING //Mojang hardcoded this -_-
    var windowSlotCount: Int = 0 //useless, seems to be part of a standard container header
    var tradeTier: Int
    var traderEid: Int
    var playerEid: Int
    var displayName: string
    var isV: Boolean2Trading
    var isWilling: Boolean
    /**
     * @var CacheableNbt
     * @phpstan-var CacheableNbt<\pocketmine\nbt\tag\CompoundTag>
     */
    offers

    override fun decodePayload(input: PacketSerializer) {
        windowId = input.getByte()
        windowType = input.getByte()
        windowSlotCount = input.getVarInt()
        tradeTier = input.getVarInt()
        traderEid = input.getEntityUniqueId()
        playerEid = input.getEntityUniqueId()
        displayName = input.getString()
        isV2Trading = input.getBool()
        isWilling = input.getBool()
        offers = new CacheableNbt (input.getNbtCompoundRoot())
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(windowId)
        output.putByte(windowType)
        output.putVarInt(windowSlotCount)
        output.putVarInt(tradeTier)
        output.putEntityUniqueId(traderEid)
        output.putEntityUniqueId(playerEid)
        output.putString(displayName)
        output.putBool(isV2Trading)
        output.putBool(isWilling)
        output.put(offers->getEncodedNbt())
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleUpdateTrade(this)
    }
}
