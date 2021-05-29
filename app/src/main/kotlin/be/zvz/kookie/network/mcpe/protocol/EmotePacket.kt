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

class EmotePacket : DataPacket(), ClientboundPacket, ServerboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.EMOTE_PACKET)

    const val FLAG_SERVER = 1 < < 0

    var entityRuntimeId: Int
    var emoteId: string
    var flags: Int

    static
    fun create(entityRuntimeId: Int, emoteId: string, flags: Int): self {
        result = new self
                result.entityRuntimeId = entityRuntimeId
        result.emoteId = emoteId
        result.flags = flags
        return result
    }

    /**
     * TODO: we can't call this getEntityRuntimeId() because of base class collision (crap architecture, thanks Shoghi)
     */
    fun getEntityRuntimeIdField(): Int {
        return entityRuntimeId
    }

    fun getEmoteId(): string {
        return emoteId
    }

    fun getFlags(): Int {
        return flags
    }

    override fun decodePayload(input: PacketSerializer) {
        entityRuntimeId = input.getEntityRuntimeId()
        emoteId = input.getString()
        flags = input.getByte()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityRuntimeId(entityRuntimeId)
        output.putString(emoteId)
        output.putByte(flags)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleEmote(this)
    }
}
