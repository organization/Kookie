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

class PlayerArmorDamagePacket : DataPacket(), ClientboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.PLAYER_ARMOR_DAMAGE_PACKET)

    const val FLAG_HEAD = 0
    const val FLAG_CHEST = 1
    const val FLAG_LEGS = 2
    const val FLAG_FEET = 3

    var headSlotDamage: Int|null
    var chestSlotDamage: Int|null
    var legsSlotDamage: Int|null
    var feetSlotDamage: Int|null

    static
    fun create(?headSlotDamage: Int, ?chestSlotDamage: Int, ?legsSlotDamage: Int, ?feetSlotDamage: Int): self {
        result = new self
                result.headSlotDamage = headSlotDamage
        result.chestSlotDamage = chestSlotDamage
        result.legsSlotDamage = legsSlotDamage
        result.feetSlotDamage = feetSlotDamage

        return result
    }

    fun getHeadSlotDamage(): ?Int
    { return headSlotDamage }

    fun getChestSlotDamage(): ?Int
    { return chestSlotDamage }

    fun getLegsSlotDamage(): ?Int
    { return legsSlotDamage }

    fun getFeetSlotDamage(): ?Int
    { return feetSlotDamage }

    fun maybeReadDamage(flags: Int, flag: Int, input: PacketSerializer): ?Int
    {
        if ((flags & (1 << flag)) !== 0){
        return input.getVarInt()
    }
        return null
    }

    override fun decodePayload(input: PacketSerializer) {
        flags = input.getByte()

        headSlotDamage = maybeReadDamage(flags, FLAG_HEAD, input)
        chestSlotDamage = maybeReadDamage(flags, FLAG_CHEST, input)
        legsSlotDamage = maybeReadDamage(flags, FLAG_LEGS, input)
        feetSlotDamage = maybeReadDamage(flags, FLAG_FEET, input)
    }

    fun composeFlag(?field: Int, flag: Int): Int {
        return field !== null ? (1 << flag) : 0
    }

    fun maybeWriteDamage(?field: Int, output: PacketSerializer) {
        if (field !== null) {
            output.putVarInt(field)
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(
            composeFlag(headSlotDamage, FLAG_HEAD) |
        composeFlag(chestSlotDamage, FLAG_CHEST) |
        composeFlag(legsSlotDamage, FLAG_LEGS) |
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
}
