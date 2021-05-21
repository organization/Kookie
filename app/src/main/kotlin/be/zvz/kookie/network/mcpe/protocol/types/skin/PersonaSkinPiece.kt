package be.zvz.kookie.network.mcpe.protocol.types.skin

class PersonaSkinPiece(
    private val pieceId: String,
    private val pieceType: String,
    private val packId: String,
    private val isDefaultPiece: Boolean,
    private val productId: String
) {

    fun getPieceId(): String = pieceId

    fun getPieceType(): String = pieceType

    fun getPackId(): String = packId

    fun isDefaultPiece(): Boolean = isDefaultPiece

    fun getProductId(): String = productId

    companion object {
        const val PIECE_TYPE_PERSONA_BODY = "persona_body"
        const val PIECE_TYPE_PERSONA_BOTTOM = "persona_bottom"
        const val PIECE_TYPE_PERSONA_EYES = "persona_eyes"
        const val PIECE_TYPE_PERSONA_FACIAL_HAIR = "persona_facial_hair"
        const val PIECE_TYPE_PERSONA_FEET = "persona_feet"
        const val PIECE_TYPE_PERSONA_HAIR = "persona_hair"
        const val PIECE_TYPE_PERSONA_MOUTH = "persona_mouth"
        const val PIECE_TYPE_PERSONA_SKELETON = "persona_skeleton"
        const val PIECE_TYPE_PERSONA_SKIN = "persona_skin"
        const val PIECE_TYPE_PERSONA_TOP = "persona_top"
    }
}
