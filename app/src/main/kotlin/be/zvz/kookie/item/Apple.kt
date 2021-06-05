package be.zvz.kookie.item

class Apple(identifier: ItemIdentifier, name: String) : Food(identifier, name) {

    override val foodRestore: Int = 4

    override val saturationRestore: Float = 2.4f
}