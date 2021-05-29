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
use fun count

class PlayerListPacket : DataPacket(), ClientboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.PLAYER_LIST_PACKET)

    const val TYPE_ADD = 0
    const val TYPE_REMOVE = 1

    /** @var PlayerListEntry[] */
    entries = []
    var type: Int

    /**
     * @param PlayerListEntry[] entries
     */
    static
    fun add(entries: array): self {
        result = new self
                result.type = TYPE_ADD
        result.entries = entries
        return result
    }

    /**
     * @param PlayerListEntry[] entries
     */
    static
    fun remove(entries: array): self {
        result = new self
                result.type = TYPE_REMOVE
        result.entries = entries
        return result
    }

    override fun decodePayload(input: PacketSerializer) {
        type = input.getByte()
        count = input.getUnsignedVarInt()
        for (i = 0 i < count ++i){
            entry = new PlayerListEntry ()

            if (type === TYPE_ADD) {
                entry.uuid = input.getUUID()
                entry.entityUniqueId = input.getEntityUniqueId()
                entry.username = input.getString()
                entry.xboxUserId = input.getString()
                entry.platformChatId = input.getString()
                entry.buildPlatform = input.getLInt()
                entry.skinData = input.getSkin()
                entry.isTeacher = input.getBool()
                entry.isHost = input.getBool()
            } else {
                entry.uuid = input.getUUID()
            }

            entries[i] = entry
        }
        if (type === TYPE_ADD) {
            for (i = 0 i < count ++i){
                entries[i]->skinData->setVerified(input.getBool())
            }
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(type)
        output.putUnsignedVarInt(count(entries))
        foreach(entries entry : as) {
            if (type === TYPE_ADD) {
                output.putUUID(entry.uuid)
                output.putEntityUniqueId(entry.entityUniqueId)
                output.putString(entry.username)
                output.putString(entry.xboxUserId)
                output.putString(entry.platformChatId)
                output.putLInt(entry.buildPlatform)
                output.putSkin(entry.skinData)
                output.putBool(entry.isTeacher)
                output.putBool(entry.isHost)
            } else {
                output.putUUID(entry.uuid)
            }
        }
        if (type === TYPE_ADD) {
            foreach(entries entry : as) {
                output.putBool(entry.skinData->isVerified())
            }
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handlePlayerList(this)
    }
}
