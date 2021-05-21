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
package be.zvz.kookie.block

enum class BlockToolType(val state: Int) {
    NONE(0),
    SWORD(1 shl 0),
    SHOVEL(1 shl 1),
    PICKAXE(1 shl 2),
    AXE(1 shl 3),
    SHEARS(1 shl 4),
    HOE(1 shl 5);

    companion object {
        @JvmStatic
        fun fromInt(state: Int) = values().first { it.state == state }
    }
}
