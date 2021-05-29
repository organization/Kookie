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

class UpdateEquipPacket : DataPacket(), ClientboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.UPDATE_EQUIP_PACKET)

    var windowId: Int
    var windowType: Int
    var windowSlotCount: Int //useless, seems to be part of a standard container header
    var entityUniqueId: Int
    /**
     * @var CacheableNbt
     * @phpstan-var CacheableNbt<\pocketmine\nbt\tag\CompoundTag>
     */
    namedtag

    override fun decodePayload(input: PacketSerializer) {
        windowId = input.getByte()
        windowType = input.getByte()
        windowSlotCount = input.getVarInt()
        entityUniqueId = input.getEntityUniqueId()
        namedtag = new CacheableNbt (input.getNbtCompoundRoot())
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(windowId)
        output.putByte(windowType)
        output.putVarInt(windowSlotCount)
        output.putEntityUniqueId(entityUniqueId)
        output.put(namedtag->getEncodedNbt())
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleUpdateEquip(this)
    }
}
