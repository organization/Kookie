package be.zvz.kookie.network.mcpe.protocol.types

import be.zvz.kookie.nbt.TreeRoot
import be.zvz.kookie.nbt.tag.Tag
import be.zvz.kookie.network.mcpe.protocol.NetworkNbtSerializer

class CacheableNbt(val root: Tag<*>) {

    var encodedNbt: String? = null
        get() {
            if (field === null) {
                field = NetworkNbtSerializer().write((TreeRoot(root)))
            }
            return field
        }
        private set
}
