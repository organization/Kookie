package be.zvz.kookie.network.mcpe.protocol.types

import be.zvz.kookie.nbt.tag.CompoundTag

class BlockPaletteEntry(private val name: String, private val states: CompoundTag) {

    fun getName(): String = name
    fun getStates(): CompoundTag = states
}
