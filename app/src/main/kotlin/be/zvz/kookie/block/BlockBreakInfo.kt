package be.zvz.kookie.block

import be.zvz.kookie.item.Item

class BlockBreakInfo(
    val hardness: Float,
    val toolType: BlockToolType,
    val toolHarvestLevel: Int = 0,
    val blastResistance: Float = hardness * 5,
) {
    fun isBreakable(): Boolean = hardness >= 0

    fun breaksInstantly(): Boolean = hardness == 0f

    fun isToolCompatible(tool: Item): Boolean = when {
        hardness < 0 -> false
        else ->
            toolType == BlockToolType.NONE || toolHarvestLevel == 0 ||
                (
                    toolType.state and tool.blockToolType.state != BlockToolType.NONE.state &&
                        tool.blockToolHarvestLevel >= toolHarvestLevel
                    )
    }

    fun getBreakTime(item: Item): Float {
        var base = if (isToolCompatible(item)) {
            hardness * 1.5f
        } else {
            hardness * 5f
        }

        val efficiency = item.getMiningEfficiency(toolType.state and item.blockToolType.state != 0)
        if (efficiency <= 0) {
            throw IllegalArgumentException("${item::class.simpleName} has invalid mining efficiency: expected >= 0, got $efficiency")
        }

        base /= efficiency

        return base
    }
}
