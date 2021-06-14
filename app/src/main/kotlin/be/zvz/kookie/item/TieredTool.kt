package be.zvz.kookie.item

abstract class TieredTool(identifier: ItemIdentifier, name: String, val tier: ToolTier) : Tool(identifier, name) {

    override val maxDurability: Int = tier.maxDurability
    override val baseMiningEfficiency: Float = tier.baseEfficiency + 0f
    override val fuelTime: Int = if (tier == ToolTier.WOOD) 200 else 0
}
