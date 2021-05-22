package be.zvz.kookie.network.mcpe.protocol.types.inventory

import be.zvz.kookie.nbt.tag.CompoundTag

data class ItemStack(
    val id: Int,
    val meta: Int,
    val count: Int,
    val blockRuntimeId: Int,
    val nbt: CompoundTag?,
    val canPlaceOn: List<String>,
    val canDestroy: List<String>,
    val shieldBlockingTick: Long?
) {
    companion object {
        fun empty(): ItemStack {
            return ItemStack(0, 0, 0, 0, null, listOf(), listOf(), null)
        }
    }
}
