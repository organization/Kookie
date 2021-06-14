package be.zvz.kookie.item

import be.zvz.kookie.block.Block
import be.zvz.kookie.block.BlockToolType
import be.zvz.kookie.entity.Entity

class Sword(identifier: ItemIdentifier, name: String, tier: ToolTier) : TieredTool(identifier, name, tier) {

    override val blockToolType: BlockToolType = BlockToolType.SWORD
    override val blockToolHarvestLevel: Int = 1
    override val attackPoint: Int = tier.baseAttackPoints
    override val baseMiningEfficiency: Float = 10f

    override fun getMiningEfficiency(isCorrectTool: Boolean): Float = super.getMiningEfficiency(isCorrectTool) * 1.5f

    override fun onDestroyBlock(block: Block): Boolean = if (!block.breakInfo.breaksInstantly()) applyDamage(2) else false

    override fun onAttackEntity(victim: Entity): Boolean = applyDamage(1)
}
