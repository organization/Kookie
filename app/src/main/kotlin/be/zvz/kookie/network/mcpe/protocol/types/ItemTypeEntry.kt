package be.zvz.kookie.network.mcpe.protocol.types

class ItemTypeEntry(private val stringId: String, private val numericId: Int, private val componentBased: Boolean) {

    fun getStringId(): String = stringId

    fun getNumericId(): Int = numericId

    fun isComponentBased(): Boolean = componentBased
}
