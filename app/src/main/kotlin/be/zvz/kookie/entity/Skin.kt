package be.zvz.kookie.entity

class Skin(
    val skinId: String,
    val skinData: String,
    val capeData: String = "",
    val geometryName: String = "",
    val geometryData: String = ""
) {

    init {
        if (skinId.trim() == "") {
            throw SkinException("Skin Id must not be empty")
        }
        if (!ACCEPTED_SKIN_SIZE.contains(skinData.length)) {
            throw SkinException("Invalid skin data size ${skinData.length} bytes (allowed size: ${ACCEPTED_SKIN_SIZE.joinToString(", ")})")
        }
        if (capeData != "" && capeData.length != 8192) {
            throw SkinException("Invalid cape data size ${capeData.length} (must be exactly 8192 bytes)")
        }
    }

    companion object {
        val ACCEPTED_SKIN_SIZE = listOf(
            64 * 32 * 4,
            64 * 64 * 4,
            128 * 128 * 4
        )
    }

    class SkinException(message: String) : RuntimeException(message)
}
