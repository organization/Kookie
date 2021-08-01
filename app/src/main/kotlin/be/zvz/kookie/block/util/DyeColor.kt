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
package be.zvz.kookie.block.util

import be.zvz.kookie.color.Color

enum class DyeColor(val id: Int, val displayName: String, val color: Color) {
    WHITE(0, "White", Color(0xf0, 0xf0, 0xf0)),
    ORANGE(1, "Orange", Color(0xf9, 0x80, 0x1d)),
    MAGENTA(2, "Magenta", Color(0xc7, 0x4e, 0xbd)),
    LIGHT_BLUE(3, "Light Blue", Color(0x3a, 0xb3, 0xda)),
    YELLOW(4, "Yellow", Color(0xfe, 0xd8, 0x3d)),
    LIME(5, "Lime", Color(0x80, 0xc7, 0x1f)),
    PINK(6, "Pink", Color(0xf3, 0x8b, 0xaa)),
    GRAY(7, "Gray", Color(0x47, 0x4f, 0x52)),
    LIGHT_GRAY(8, "Light Gray", Color(0x9d, 0x9d, 0x97)),
    CYAN(9, "Cyan", Color(0x16, 0x9c, 0x9c)),
    PURPLE(10, "Purple", Color(0x89, 0x32, 0xb8)),
    BLUE(11, "Blue", Color(0x3c, 0x44, 0xaa)),
    BROWN(12, "Brown", Color(0x83, 0x54, 0x32)),
    GREEN(13, "Green", Color(0x5e, 0x7c, 0x16)),
    RED(14, "Red", Color(0xb0, 0x2e, 0x26)),
    BLACK(15, "Black", Color(0x1d, 0x1d, 0x21));

    val invertedId: Int = id.inv() and 0xf

    companion object {
        @JvmStatic
        fun fromId(id: Int) = values().find { it.id == id }

        @JvmStatic
        fun fromInvertedId(invertedId: Int) = values().find { it.invertedId == invertedId }
    }
}
