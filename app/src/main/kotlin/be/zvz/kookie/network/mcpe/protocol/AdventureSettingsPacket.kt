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
import be.zvz.kookie.network.mcpe.protocol.types.PlayerPermissions

@ProtocolIdentify(ProtocolInfo.IDS.ADVENTURE_SETTINGS_PACKET)
class AdventureSettingsPacket : DataPacket(), ClientboundPacket, ServerboundPacket {
    private var flags: Int = 0
    private var flags2: Int = -1
    var commandPermission: Int = PERMISSION_NORMAL
    var playerPermission: Int = PlayerPermissions.MEMBER
    var customFlags: Int = 0
    var entityUniqueId: Long = 0

    override fun decodePayload(input: PacketSerializer) {
        flags = input.getUnsignedVarInt()
        commandPermission = input.getUnsignedVarInt()
        flags2 = input.getUnsignedVarInt()
        playerPermission = input.getUnsignedVarInt()
        customFlags = input.getUnsignedVarInt()
        entityUniqueId = input.getLLong()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putUnsignedVarInt(flags)
        output.putUnsignedVarInt(commandPermission)
        output.putUnsignedVarInt(flags2)
        output.putUnsignedVarInt(playerPermission)
        output.putUnsignedVarInt(customFlags)
        output.putLLong(entityUniqueId)
    }

    fun getFlag(flag: Int): Boolean {
        if ((flag and BITFLAG_SECOND_SET) != 0) {
            return (flags2 and flag) != 0
        }

        return (flags and flag) != 0
    }

    fun setFlag(flag: Int, value: Boolean) {
        when (Pair(flags and BITFLAG_SECOND_SET, value)) {
            Pair(0, true) -> flags = flags or flag
            Pair(0, false) -> flags = flags and flag.inv()
            Pair(1, true) -> flags2 = flags2 or flag
            Pair(1, false) -> flags2 = flags2 and flag.inv()
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleAdventureSettings(this)
    }

    companion object {
        const val PERMISSION_NORMAL = 0
        const val PERMISSION_OPERATOR = 1
        const val PERMISSION_HOST = 2
        const val PERMISSION_AUTOMATION = 3
        const val PERMISSION_ADMIN = 4

        const val BITFLAG_SECOND_SET = 1 shl 16

        const val WORLD_IMMUTABLE = 0x01
        const val NO_PVP = 0x02

        const val AUTO_JUMP = 0x20
        const val ALLOW_FLIGHT = 0x40
        const val NO_CLIP = 0x80
        const val WORLD_BUILDER = 0x100
        const val FLYING = 0x200
        const val MUTED = 0x400

        const val MINE = 0x01 or BITFLAG_SECOND_SET
        const val DOORS_AND_SWITCHES = 0x02 or BITFLAG_SECOND_SET
        const val OPEN_CONTAINERS = 0x04 or BITFLAG_SECOND_SET
        const val ATTACK_PLAYERS = 0x08 or BITFLAG_SECOND_SET
        const val ATTACK_MOBS = 0x10 or BITFLAG_SECOND_SET
        const val OPERATOR = 0x20 or BITFLAG_SECOND_SET
        const val TELEPORT = 0x80 or BITFLAG_SECOND_SET
        const val BUILD = 0x100 or BITFLAG_SECOND_SET
        const val DEFAULT = 0x200 or BITFLAG_SECOND_SET
    }
}
