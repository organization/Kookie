package be.zvz.kookie.world.format.dummy

class PalettedBlockArray(val id: Int) {
    val palette: List<Int> = listOf()

    fun collectGarbage(): Unit = TODO("Dummy class")

    fun get(x: Int, y: Int, z: Int): Int = TODO("Dummy class")
    fun set(x: Int, y: Int, z: Int, block: Int): Int = TODO("Dummy class")
}
