package be.zvz.kookie.item

import be.zvz.kookie.block.Block
import be.zvz.kookie.block.BlockToolType
import be.zvz.kookie.entity.Entity

class Axe(identifier: ItemIdentifier, name: String, tier: ToolTier) : TieredTool(identifier, name, tier) {

    override val blockToolType: BlockToolType = BlockToolType.AXE
    override val blockToolHarvestLevel: Int = tier.harvestLevel
    override val attackPoint: Int = tier.baseAttackPoints - 1

    override fun onDestroyBlock(block: Block): Boolean = if (!block.breakInfo.breaksInstantly()) applyDamage(1) else false

    override fun onAttackEntity(victim: Entity): Boolean = applyDamage(2)
}
