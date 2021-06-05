package be.zvz.kookie.color

class Color(val r: Int, val g: Int, val b: Int, val a: Int = 0xff) {

    fun toARGB(): Int {
        return (this.a shl 24) or (this.r shl 16) or (this.g shl 8) or this.b
    }

    fun toRGBA(): Int {
        return (this.r shl 24) or (this.g shl 16) or (this.b shl 8) or this.a
    }

    companion object {

        fun mix(color1: Color, vararg colors: Color): Color {
            val colorList = colors.toMutableList()
            colorList.add(color1)
            val count = colorList.size

            var (a, r, g, b) = listOf(0, 0, 0, 0)
            colorList.forEach {
                a += it.a
                r += it.r
                g += it.g
                b += it.b
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