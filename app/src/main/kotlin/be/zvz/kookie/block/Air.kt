package be.zvz.kookie.block

import be.zvz.kookie.math.AxisAlignedBB

class Air(
    idInfo: BlockIdentifier,
    name: String,
    breakInfo: BlockBreakInfo
) : Transparent(idInfo, name, breakInfo) {

    override fun canBeFlowedInto(): Boolean = true

    override fun canBeReplaced(): Boolean = true

    override fun canBePlaced(): Boolean = false

    override fun isSolid(): Boolean = false

    override fun recalculateCollisionBoxes(): List<AxisAlignedBB> = listOf()
}