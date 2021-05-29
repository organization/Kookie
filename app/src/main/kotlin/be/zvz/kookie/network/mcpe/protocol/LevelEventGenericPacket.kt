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

class LevelEventGenericPacket : DataPacket(), ClientboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.LEVEL_EVENT_GENERIC_PACKET)

    var eventId: Int
    /**
     * @var CacheableNbt
     * @phpstan-var CacheableNbt<\pocketmine\nbt\tag\CompoundTag>
     */
    eventData

    static
    fun create(eventId: Int, data: CompoundTag): self {
        result = new self
                result.eventId = eventId
        result.eventData = new CacheableNbt (data)
        return result
    }

    fun getEventId(): Int {
        return eventId
    }

    /**
     * @phpstan-return CacheableNbt<\pocketmine\nbt\tag\CompoundTag>
     */
    fun getEventData(): CacheableNbt {
        return eventData
    }

    override fun decodePayload(input: PacketSerializer) {
        eventId = input.getVarInt()
        eventData = new CacheableNbt (input.getNbtCompoundRoot())
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putVarInt(eventId)
        output.put(eventData->getEncodedNbt())
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleLevelEventGeneric(this)
    }
}
