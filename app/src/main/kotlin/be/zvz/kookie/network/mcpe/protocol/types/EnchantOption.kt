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
        @JvmStatic
        fun readEnchantList(input: PacketSerializer): List<Enchant> = mutableListOf<Enchant>().apply {
            repeat(input.getUnsignedVarInt()) {
                add(Enchant.read(input))
            }
        }

        @JvmStatic
        fun read(input: PacketSerializer) = EnchantOption(
            cost = input.getUnsignedVarInt(),
            slotFlags = input.getLInt(),

            equipActivatedEnchantments = readEnchantList(input),
            heldActivatedEnchantments = readEnchantList(input),
            selfActivatedEnchantments = readEnchantList(input),

            name = input.getString(),
            optionId = input.readGenericTypeNetworkId()
        )

        @JvmStatic
        fun writeEnchantList(output: PacketSerializer, list: List<Enchant>) {
            output.putUnsignedVarInt(list.size)
            list.forEach {
                it.write(output)
            }
        }
    }
}
