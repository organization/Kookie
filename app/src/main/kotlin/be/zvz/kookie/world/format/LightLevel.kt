package be.zvz.kookie.world.format

data class LightLevel(val value: Int) {
    init {
        if (value > MAX) {
            throw IllegalArgumentException("value out of range")
        }
    }

    companion object {
        const val MAX = 15
    }
}
