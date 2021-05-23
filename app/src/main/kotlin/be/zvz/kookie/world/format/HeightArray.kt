package be.zvz.kookie.world.format

class HeightArray(val values: MutableList<Int> = mutableListOf()) {
    init {
        if (values.size != 256) {
            throw IllegalArgumentException("Expected exactly 256 values")
        }
    }

    fun get(x: Int, z: Int): Int = values[idx(x, z)]

    fun set(x: Int, z: Int, height: Int) {
        values[idx(x, z)] = height
    }

    fun clone(): HeightArray {
        return HeightArray(values)
    }

    companion object {
        fun fill(value: Int): HeightArray {
            val list = MutableList(255) { value }
            return HeightArray(list)
        }

        private fun idx(x: Int, z: Int): Int {
            if (x !in 0 until 16 || z !in 0 until 16) {
                throw IllegalArgumentException("x and z must be in the range 0-15")
            }

            return (z shl 4) or x
        }
    }
}
