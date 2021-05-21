package be.zvz.kookie.network.mcpe.protocol.types.skin

class SkinImage(private val height: Int, private val width: Int, private val data: String) {

    init {
        if (height < 0 || width < 0) {
            throw SkinImageException("height and width cannot be negative")
        }
        val expected = height * width * 4
        val actual = data.length
        if (expected != actual) {
            throw SkinImageException("Data should be exactly $expected bytes, got $actual bytes")
        }
    }

    fun getHeight(): Int = height

    fun getWidth(): Int = width

    fun getData(): String = data

    companion object {
        fun fromLegacy(data: String): SkinImage {
            return when (data.length) {
                64 * 32 * 4 -> SkinImage(32, 64, data)
                64 * 64 * 4 -> SkinImage(64, 64, data)
                128 * 128 * 4 -> SkinImage(128, 128, data)
                else -> throw SkinImageException("Unknown size")
            }
        }
    }

    class SkinImageException(message: String) : RuntimeException(message)
}
