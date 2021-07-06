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

enum class DyeColor(val color: Color) {
    WHITE(Color(0xf0, 0xf0, 0xf0)),
    ORANGE(Color(0xf9, 0x80, 0x1d)),
    MAGENTA(Color(0xc7, 0x4e, 0xbd)),
    LIGHT_BLUE(Color(0x3a, 0xb3, 0xda)),
    YELLOW(Color(0xfe, 0xd8, 0x3d)),
    LIME(Color(0x80, 0xc7, 0x1f)),
    PINK(Color(0xf3, 0x8b, 0xaa)),
    GRAY(Color(0x47, 0x4f, 0x52)),
    LIGHT_GRAY(Color(0x9d, 0x9d, 0x97)),
    CYAN(Color(0x16, 0x9c, 0x9c)),
    PURPLE(Color(0x89, 0x32, 0xb8)),
    BLUE(Color(0x3c, 0x44, 0xaa)),
    BROWN(Color(0x83, 0x54, 0x32)),
    GREEN(Color(0x5e, 0x7c, 0x16)),
    RED(Color(0xb0, 0x2e, 0x26)),
    BLACK(Color(0x1d, 0x1d, 0x21)),
}
