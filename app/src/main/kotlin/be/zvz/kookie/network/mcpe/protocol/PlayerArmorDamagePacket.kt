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

@ProtocolIdentify(ProtocolInfo.IDS.PLAYER_ARMOR_DAMAGE_PACKET)
class PlayerArmorDamagePacket : DataPacket(), ClientboundPacket {

    var headSlotDamage: Int? = null
    var chestSlotDamage: Int? = null
    var legsSlotDamage: Int? = null
    var feetSlotDamage: Int? = null

    fun maybeReadDamage(flags: Int, flag: Int, input: PacketSerializer): Int? {
        if (flags and (1 shl flag) != 0) {
            return input.getVarInt()
        }
        return null
    }

    override fun decodePayload(input: PacketSerializer) {
        val flags = input.getByte()

        headSlotDamage = maybeReadDamage(flags, FLAG_HEAD, input)
        chestSlotDamage = maybeReadDamage(flags, FLAG_CHEST, input)
        legsSlotDamage = maybeReadDamage(flags, FLAG_LEGS, input)
        feetSlotDamage = maybeReadDamage(flags, FLAG_FEET, input)
    }

    fun composeFlag(field: Int?, flag: Int): Int {
        return field?.let { 1 shl flag } ?: 0
    }

    fun maybeWriteDamage(field: Int?, output: PacketSerializer) {
        if (field !== null) {
            output.putVarInt(field)
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(
            composeFlag(headSlotDamage, FLAG_HEAD) or
                composeFlag(chestSlotDamage, FLAG_CHEST) or
                composeFlag(legsSlotDamage, FLAG_LEGS) or
                composeFlag(feetSlotDamage, FLAG_FEET)
        )

        maybeWriteDamage(headSlotDamage, output)
        maybeWriteDamage(chestSlotDamage, output)
        maybeWriteDamage(legsSlotDamage, output)
        maybeWriteDamage(feetSlotDamage, output)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handlePlayerArmorDamage(this)
    }

    companion object {
        const val FLAG_HEAD = 0
        const val FLAG_CHEST = 1
        const val FLAG_LEGS = 2
        const val FLAG_FEET = 3

        fun create(headSlotDamage: Int?, chestSlotDamage: Int?, legsSlotDamage: Int?, feetSlotDamage: Int?) =
            PlayerArmorDamagePacket().apply {
                this.headSlotDamage = headSlotDamage
                this.chestSlotDamage = chestSlotDamage
                this.legsSlotDamage = legsSlotDamage
                this.feetSlotDamage = feetSlotDamage
            }
    }
}
