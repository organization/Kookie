package be.zvz.kookie.color

import java.awt.Color as AwtColor

class Color @JvmOverloads constructor(r: Int, g: Int, b: Int, a: Int = 0xff) : AwtColor(r, g, b, a) {
    constructor(awtColor: AwtColor) : this(awtColor.red, awtColor.green, awtColor.blue, awtColor.alpha)

    fun toARGB(): Int {
        return (this.alpha shl 24) or (this.red shl 16) or (this.green shl 8) or this.blue
    }

    fun toRGBA(): Int {
        return (this.red shl 24) or (this.green shl 16) or (this.blue shl 8) or this.alpha
    }

    companion object {

        fun mix(color1: Color, vararg colors: Color): Color {
            val colorList = colors.toMutableList()
            colorList.add(color1)
            val count = colorList.size

            var (a, r, g, b) = listOf(0, 0, 0, 0)
            colorList.forEach {
                a += it.alpha
                r += it.red
                g += it.green
                b += it.blue
            }

            return Color(r / count, g / count, b / count, a / count)
        }

        fun fromRGB(code: Int): Color {
            return Color((code shr 16) and 0xff, (code shr 8) and 0xff, code and 0xff)
        }

        fun fromARGB(code: Int): Color {
            return Color((code shr 16) and 0xff, (code shr 8) and 0xff, code and 0xff, (code shr 24) and 0xff)
        }

        fun fromRGBA(c: Int): Color {
            return Color((c shr 24) and 0xff, (c shr 16) and 0xff, (c shr 8) and 0xff, c and 0xff)
        }
    }
}
