package be.zvz.kookie.network.mcpe.protocol.types

import be.zvz.kookie.nbt.TreeRoot
import be.zvz.kookie.nbt.tag.Tag
import be.zvz.kookie.network.mcpe.protocol.NetworkNbtSerializer

class CacheableNbt(val root: Tag<*>) {

    val encodedNbt: String by lazy { NetworkNbtSerializer().write((TreeRoot(root))) }
}
