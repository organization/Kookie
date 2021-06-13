package be.zvz.kookie.item

enum class ToolTier(val harvestLevel: Int, val maxDurability: Int, val baseAttackPoints: Int, val baseEfficiency: Int) {
    WOOD(1, 60, 5, 2),
    GOLD(2, 33, 5, 12),
    STONE(3, 132, 6, 4),
    IRON(4, 251, 7, 6),
    DIAMOND(5, 1562, 8, 8),
    NETHERITE(6, 2032, 9, 9);
}
