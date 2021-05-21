package be.zvz.kookie.network.mcpe.protocol.types.skin

import java.util.*

class SkinData(
    private val skinId: String,
    private val playFabId: String,
    private val resourcePatch: String,
    private val skinImage: SkinImage,
    private val animations: MutableList<SkinAnimation> = mutableListOf(),
    private var capeImage: SkinImage? = null,
    private val geometryData: String = "",
    private val animationData: String = "",
    private val premium: Boolean = false,
    private val persona: Boolean = false,
    private val personaCapeOnClassic: Boolean = false,
    private val capeId: String = "",
    private var fullSkinId: String? = null,
    private val armSize: String = ARM_SIZE_WIDE,
    private val skinColor: String = "",
    private val personaPieces: MutableList<PersonaSkinPiece> = mutableListOf(),
    private val pieceTintColors: MutableList<PersonaPieceTintColor> = mutableListOf(),
    private var isVerified: Boolean = true
) {

    init {
        if (capeImage == null) {
            capeImage = SkinImage(0, 0, "")
        }
        if (fullSkinId == null) {
            fullSkinId = UUID.randomUUID().toString()
        }
    }

    fun getSkinId(): String = skinId

    fun getPlayFabId(): String = playFabId

    fun getResourcePatch(): String = resourcePatch

    fun getSkinImage(): SkinImage = skinImage

    fun getAnimations(): MutableList<SkinAnimation> = animations

    fun getCapeImage(): SkinImage? = capeImage // HACK: this should never null because we check it on init

    fun getGeometryData(): String = geometryData

    fun getAnimationData(): String = animationData

    fun isPremium(): Boolean = premium

    fun isPersona(): Boolean = persona

    fun isPersonaCapeOnClassic(): Boolean = personaCapeOnClassic

    fun getCapeId(): String = capeId

    fun getFullSkinId(): String? = fullSkinId // HACK: this should never null because we check it on init

    fun getArmSize(): String = armSize

    fun getSkinColor(): String = skinColor

    fun getPersonaPieces(): MutableList<PersonaSkinPiece> = personaPieces

    fun getPieceTintColors(): MutableList<PersonaPieceTintColor> = pieceTintColors

    fun isVerified(): Boolean = isVerified

    fun setVerified(verified: Boolean) {
        isVerified = verified
    }

    companion object {
        const val ARM_SIZE_SLIM = "slim"
        const val ARM_SIZE_WIDE = "wide"
    }
}
