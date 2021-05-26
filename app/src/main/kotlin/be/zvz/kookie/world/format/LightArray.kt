package be.zvz.kookie.world.format

class LightArray {
    private val values: IntArray = IntArray(DATA_SIZE)

    private constructor(payload: String) {
        if (payload.length != DATA_SIZE) {
            throw IllegalArgumentException("Payload size must be $DATA_SIZE bytes, but got ${payload.length} bytes")
        }
        payload.forEachIndexed { index, value ->
            values[index] = value.code
        }
    }

    private constructor(fillValue: Int) {
        values.fill(fillValue shl 4 or fillValue)
    }

    fun get(x: Int, y: Int, z: Int): Int {
        val (offset, shift) = index(x, y, z)
        return values[offset] shr shift and 0xf
    }

    fun set(x: Int, y: Int, z: Int, level: LightLevel) {
        val (offset, shift) = index(x, y, z)
        val byte = values[offset]
        values[offset] = byte and (0xf shl shift).inv() or (level.value shl shift)
    }

    fun getData(): String = this.toString()

    override fun toString(): String = String(
        CharArray(DATA_SIZE).apply {
            values.forEachIndexed { index, value ->
                set(index, value.toChar())
            }
        }
    )

    companion object {
        private const val DATA_SIZE = 2048
        private fun index(x: Int, y: Int, z: Int): Pair<Int, Int> =
            Pair(x shl 7 or (z shl 3) or (y shr 1), y and 1 shl 2)

        fun fill(level: Int): LightArray {
            if (level > LightLevel.MAX) {
                throw IllegalArgumentException("Light level must be max ${LightLevel.MAX}")
            }

            return LightArray(level)
        }
    }
}
