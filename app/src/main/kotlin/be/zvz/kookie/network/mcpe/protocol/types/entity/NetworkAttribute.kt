package be.zvz.kookie.network.mcpe.protocol.types.entity

class NetworkAttribute(
    private val id: String,
    private val min: Float,
    private val max: Float,
    private val current: Float,
    private val default: Float
) {

    fun getId(): String = id

    fun getMin(): Float = min

    fun getMax(): Float = max

    fun getCurrent(): Float = current

    fun getDefault(): Float = default
}
