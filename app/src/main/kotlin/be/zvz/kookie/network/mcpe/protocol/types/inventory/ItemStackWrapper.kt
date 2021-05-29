package be.zvz.kookie.network.mcpe.protocol.types.inventory

import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

data class ItemStackWrapper(val stackId: Int, val itemStack: ItemStack) {
    fun write(out: PacketSerializer) {
        out.putItemStack(itemStack) {
            it.putBoolean(stackId != 0)
            if (stackId != 0) {
                it.writeGenericTypeNetworkId(stackId)
            }
        }
    }

    companion object {
        fun read(input: PacketSerializer): ItemStackWrapper {
            var stackId = 0
            val stack = input.getItemStack {
                val hasNetId = it.getBoolean()
                if (hasNetId) {
                    stackId = it.readGenericTypeNetworkId()
                }
            }
            return ItemStackWrapper(stackId, stack)
        }
    }
}
