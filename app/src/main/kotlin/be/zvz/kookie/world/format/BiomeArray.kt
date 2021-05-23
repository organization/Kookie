package be.zvz.kookie.world.format

class BiomeArray(var payload: String) {
    init {
        if (payload.length != 256) {
            throw IllegalArgumentException("Biome array is expected to be exactly 256 bytes")
        }
    }

    companion object {
        fun fill(biomeId: Int): BiomeArray {
            return BiomeArray(biomeId.toString().repeat(256))
        }

        fun idx(x: Int, z: Int): Int {
            if (x !in 0..15 || z !in 0..15) {
                throw IllegalArgumentException("x and z must be in the range 0-15")
            }

            return (z shl 4) or x
        }
    }

    fun get(x: Int, z: Int): Int = payload[idx(x, z)].code

    fun set(x: Int, z: Int, biomeId: Int) {
        if (biomeId !in 0 until 256) {
            throw IllegalArgumentException("Biome ID must be in the range 0-255")
        }

        payload = payload.replaceRange(idx(x, z)..idx(x, z), biomeId.toString())
    }
}
