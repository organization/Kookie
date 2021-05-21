package be.zvz.kookie.network.mcpe.protocol.types.skin

class SkinAnimation(
    private val image: SkinImage,
    private val type: Int,
    private val frames: Float,
    private val expressionType: Int
) {

    fun getImage(): SkinImage = image

    fun getType(): Int = type

    fun getFrames(): Float = frames

    fun getExpressionType(): Int = expressionType

    companion object {
        const val TYPE_HEAD = 1
        const val TYPE_BODY_32 = 2
        const val TYPE_BODY_64 = 3

        const val EXPRESSION_LINEAR = 0 // ???
        const val EXPRESSION_BLINKING = 1
    }
}
