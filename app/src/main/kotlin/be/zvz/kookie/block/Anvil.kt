package be.zvz.kookie.block

//import be.zvz.kookie.block.inventory.AnvilInventory
//import be.zvz.kookie.block.util.BlockDataSerializer
import be.zvz.kookie.block.util.Fallable
import be.zvz.kookie.block.util.FallableHelper
import be.zvz.kookie.block.util.HorizontalFacingHelper
import be.zvz.kookie.math.Axis
import be.zvz.kookie.math.AxisAlignedBB
import be.zvz.kookie.math.Facing

class Anvil(
    idInfo: BlockIdentifier,
    name: String,
    breakInfo: BlockBreakInfo
) : Transparent(idInfo, name, breakInfo), Fallable, FallableHelper, HorizontalFacingHelper {
    private var damage: Int = 0
    var facing: Facing = HorizontalFacingHelper.facing

    override fun writeStateToMeta(): Int {
        //TODO: not yet implemented
    }

    override fun readStateFromData(id: Int, stateMeta: Int) {
        //TODO: remove comment after implementing functions
        //facing = BlockDataSerializer.readLegacyHorizontalFacing(stateMeta and 0x3)
        //damage = BlockDataSerializer.readBoundedInt("damage", stateMeta shr 2, 0, 2)
    }

    override fun getStateBitmask(): Int = 0b1111

    override fun getNonPersistentStateBitmask(): Int = 0b11

    fun getDamage(): Int = damage

    fun setDamage(damage: Int): Anvil = apply {
        if(damage < 0 || damage > 2) {
            throw IllegalArgumentException("Damage must be in range 0-2")
        }
        this.damage = damage
    }

    override fun recalculateCollisionBoxes(): List<AxisAlignedBB> = listOf(
        AxisAlignedBB.one().squash( //TODO: Conflict between squash and Facing.axis
            Facing.axis(
                Facing.rotate(
                    facing, Axis.Y ,false
                )
            ),
            1/8)
    )

    override fun tickFalling(): Block? = null
}
