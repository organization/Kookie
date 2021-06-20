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
package be.zvz.kookie.color

import java.awt.Color as AwtColor

class Color(val awtColor: AwtColor) {
    val red: Int
        get() = awtColor.red

    val green: Int
        get() = awtColor.green

    val blue: Int
        get() = awtColor.blue

    val alpha: Int
        get() = awtColor.alpha

    @JvmOverloads
    constructor(r: Int, g: Int, b: Int, a: Int = 0xff) : this(AwtColor(r, g, b, a))

    fun toARGB(): Int {
        return (this.alpha shl 24) or (this.red shl 16) or (this.green shl 8) or this.blue
    }

    fun toRGBA(): Int {
        return (this.red shl 24) or (this.green shl 16) or (this.blue shl 8) or this.alpha
    }

    companion object {
        @JvmStatic
        fun mix(vararg colors: Color): Color {
            val count = colors.size
            return Color(
                colors.sumOf { it.red } / count,
                colors.sumOf { it.green } / count,
                colors.sumOf { it.blue } / count,
                colors.sumOf { it.alpha } / count
            )
        }

        @JvmStatic fun fromRGB(code: Int) = Color(code shr 16 and 0xff, code shr 8 and 0xff, code and 0xff)

        @JvmStatic fun fromARGB(code: Int) = Color(code shr 16 and 0xff, code shr 8 and 0xff, code and 0xff, code shr 24 and 0xff)

        @JvmStatic fun fromRGBA(c: Int) = Color(c shr 24 and 0xff, c shr 16 and 0xff, c shr 8 and 0xff, c and 0xff)
    }
}
