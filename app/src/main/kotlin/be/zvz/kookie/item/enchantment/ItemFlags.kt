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
package be.zvz.kookie.item.enchantment

enum class ItemFlags(val flags: Int) {
    COMMON(10),
    NONE(0x0),
    ALL(0xffff),
    HEAD(0x1),
    TORSO(0x2),
    LEGS(0x4),
    FEET(0x8),
    ARMOR(HEAD.flags or TORSO.flags or LEGS.flags or FEET.flags),
    SWORD(0x10),
    BOW(0x20),
    HOE(0x40),
    SHEARS(0x80),
    FLINT_AND_STEEL(0x100),
    TOOL(HOE.flags or SHEARS.flags or FLINT_AND_STEEL.flags),
    AXE(0x200),
    PICKAXE(0x400),
    SHOVEL(0x800),
    DIG(AXE.flags or PICKAXE.flags or SHOVEL.flags),
    FISHING_ROD(0x1000),
    CARROT_STICK(0x2000),
    ELYTRA(0x4000),
    TRIDENT(0x8000);

    companion object {
        private val VALUE = values()
        @JvmStatic
        fun from(findValue: Int): ItemFlags = VALUE.first { it.flags == findValue }
    }
}
