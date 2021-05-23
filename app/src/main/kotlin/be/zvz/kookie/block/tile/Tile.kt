package be.zvz.kookie.block.tile

import be.zvz.kookie.block.Block
import be.zvz.kookie.item.Item
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.timings.Timings
import be.zvz.kookie.world.Position
import be.zvz.kookie.world.World

abstract class Tile(world: World, pos: Vector3) {
    protected val timings = Timings.getTileEntityTimings(this)

    val pos = Position.fromObject(pos, world)
    val closed: Boolean = false

    abstract fun readSaveData(nbt: CompoundTag)

    protected abstract fun writeSaveData(nbt: CompoundTag)

    fun saveNBT(): CompoundTag {
        val nbt = CompoundTag.create()
        nbt.apply {
            setString(TAG_ID, TileFactory.getSaveId(this.javaClass.name))
            setInt(TAG_X, pos.floor().x.toInt())
            setInt(TAG_Y, pos.floor().y.toInt())
            setInt(TAG_Z, pos.floor().z.toInt())
        }
        writeSaveData(nbt)

        return nbt
    }

    fun getCleanedNBT(): CompoundTag? {
        val tag = CompoundTag()

        writeSaveData(tag)

        return if (tag.count() > 0) tag else null
    }

    fun copyDataFromItem(item: Item) {
        item.getCustomBlockData()?.let {
            readSaveData(it)
        }
    }

    fun getBlock(): Block = TODO("get block from world")

    companion object {
        const val TAG_ID = "id"
        const val TAG_X = "x"
        const val TAG_Z = "z"
        const val TAG_Y = "y"
    }
}
