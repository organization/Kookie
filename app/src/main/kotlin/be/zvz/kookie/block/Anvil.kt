package be.zvz.kookie.block

//import be.zvz.kookie.block.inventory.AnvilInventory
//import be.zvz.kookie.block.util.BlockDataSeroializer
import be.zvz.kookie.block.util.Fallable
import be.zvz.kookie.block.util.FallableHelper
import be.zvz.kookie.math.Facing

class Anvil(
    idInfo: BlockIdentifier,
    name: String,
    breakInfo: BlockBreakInfo
) : Transparent(idInfo, name, breakInfo), Fallable, FallableHelper {
    private var damage: Int = 0

    override fun writeStateToMeta(): Int {
        //not yet implemented
    }

    override fun readStateFromData(id: Int, stateMeta: Int) {
        Facing
    }

    override fun tickFalling(): Block? = null
}
