package be.zvz.kookie.network.mcpe.protocol.types.skin

import java.util.*

class SkinData(
    val skinId: String,
    val playFabId: String,
    val resourcePatch: String,
    val skinImage: SkinImage,
    val animations: MutableList<SkinAnimation> = mutableListOf(),
    var capeImage: SkinImage? = null,
    val geometryData: String = "",
    val animationData: String = "",
    val premium: Boolean = false,
    val persona: Boolean = false,
    val personaCapeOnClassic: Boolean = false,
    val capeId: String = "",
    var fullSkinId: String? = null,
    val armSize: String = ARM_SIZE_WIDE,
    val skinColor: String = "",
    val personaPieces: MutableList<PersonaSkinPiece> = mutableListOf(),
    val pieceTintColors: MutableList<PersonaPieceTintColor> = mutableListOf(),
    var isVerified: Boolean = true
) {

    init {
        if (capeImage == null) {
            capeImage = SkinImage(0, 0, "")
        }
        if (fullSkinId == null) {
            fullSkinId = UUID.randomUUID().toString()
        }
    }

    companion object {
        const val ARM_SIZE_SLIM = "slim"
        const val ARM_SIZE_WIDE = "wide"
    }
}
