package be.zvz.kookie.block

enum class BlockToolType(val state: Int) {
    NONE(0),
    SWORD(1 shl 0),
    SHOVEL(1 shl 1),
    PICKAXE(1 shl 2),
    AXE(1 shl 3),
    SHEARS(1 shl 4),
    HOE(1 shl 5);

    companion object {
        fun fromInt(state: Int) = BlockToolType.values().first { it.state == state }
    }
}
