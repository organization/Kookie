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
package be.zvz.kookie.network.mcpe.protocol.types

import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

class EnchantOption(
    val cost: Int,
    val slotFlags: Int,
    val equipActivatedEnchantments: List<Enchant>,
    val heldActivatedEnchantments: List<Enchant>,
    val selfActivatedEnchantments: List<Enchant>,
    val name: String,
    val optionId: Int
) {

    fun write(output: PacketSerializer) {
        output.putUnsignedVarInt(cost)

        output.putLInt(slotFlags)
        writeEnchantList(output, equipActivatedEnchantments)
        writeEnchantList(output, heldActivatedEnchantments)
        writeEnchantList(output, selfActivatedEnchantments)

        output.putString(name)
        output.writeGenericTypeNetworkId(optionId)
    }

    companion object {
        fun readEnchantList(input: PacketSerializer): List<Enchant> = mutableListOf<Enchant>().apply {
            for (i in 0 until input.getUnsignedVarInt()) {
                add(Enchant.read(input))
            }
        }

        fun read(input: PacketSerializer): EnchantOption {
            val cost = input.getUnsignedVarInt()

            val slotFlags = input.getLInt()
            val equipActivatedEnchants = readEnchantList(input)
            val heldActivatedEnchants = readEnchantList(input)
            val selfActivatedEnchants = readEnchantList(input)

            val name = input.getString()
            val optionId = input.readGenericTypeNetworkId()

            return EnchantOption(
                cost,
                slotFlags,
                equipActivatedEnchants,
                heldActivatedEnchants,
                selfActivatedEnchants,
                name,
                optionId
            )
        }

        fun writeEnchantList(output: PacketSerializer, list: List<Enchant>) {
            output.putUnsignedVarInt(list.size)
            list.forEach {
                it.write(output)
            }
        }
    }
}
