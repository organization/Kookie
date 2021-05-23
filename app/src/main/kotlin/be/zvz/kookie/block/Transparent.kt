package be.zvz.kookie.block

open class Transparent(
    idInfo: BlockIdentifier,
    name: String,
    breakInfo: BlockBreakInfo,
) : Block(idInfo, name, breakInfo) {
    fun isTransparent(): Boolean = true
}
