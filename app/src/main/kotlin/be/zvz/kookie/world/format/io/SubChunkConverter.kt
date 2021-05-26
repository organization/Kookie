package be.zvz.kookie.world.format.io

import be.zvz.kookie.world.format.BlockArrayContainer
import be.zvz.kookie.world.format.PalettedBlockArray
import kotlin.jvm.internal.Ref

object SubChunkConverter {
    fun convertSubChunkXZY(idArray: String, metaArray: String): PalettedBlockArray {
        return PalettedBlockArray(
            convert(
                idArray, metaArray, 0, getIndexSubChunkXZY,
                mapper,
                false
            )
        )
    }

    fun convertSubChunkYZX(idArray: String, metaArray: String): PalettedBlockArray {
        return PalettedBlockArray(
            convert(
                idArray, metaArray, 0, getIndexSubChunkXZY,
                mapper,
                true
            )
        )
    }

    fun convertSubChunkFromLegacyColumn(idArray: String, metaArray: String, yOffset: Int): PalettedBlockArray {
        return PalettedBlockArray(
            convert(
                idArray, metaArray, yOffset, getIndexLegacyColumnXZY,
                mapper,
                true
            )
        )
    }

    private val mapper: (Int, Int) -> Long = { id: Int, meta: Int ->
        id.toLong() shl 4 or meta.toLong()
    }

    private val getIndexSubChunkXZY: (Int, Int, Int, Ref.IntRef, Ref.IntRef, Ref.IntRef, Int) -> Unit = {
        x: Int,
        y: Int,
        z: Int,
        id1Idx: Ref.IntRef,
        id2Idx: Ref.IntRef,
        metaIdx: Ref.IntRef,
        _: Int ->
        id1Idx.element = x shl 8 or (z shl 4) or (y shl 1)
        id2Idx.element = id1Idx.element or 1
        metaIdx.element = id1Idx.element shr 1
    }

    private val getIndexLegacyColumnXZY: (Int, Int, Int, Ref.IntRef, Ref.IntRef, Ref.IntRef, Int) -> Unit = {
        x: Int,
        y: Int,
        z: Int,
        id1Idx: Ref.IntRef,
        id2Idx: Ref.IntRef,
        metaIdx: Ref.IntRef,
        yOffset: Int ->
        id1Idx.element = x shl 11 or (z shl 7) or (yOffset shl 4) or (y shl 1)
        id2Idx.element = id1Idx.element or 1
        metaIdx.element = id1Idx.element shr 1
    }

    private fun <Block, getIndexArg> convert(
        idArray: String,
        metaArray: String,
        extraArg: getIndexArg,
        getIndex: (Int, Int, Int, Ref.IntRef, Ref.IntRef, Ref.IntRef, getIndexArg) -> Unit,
        mapper: (Int, Int) -> Block,
        isYZX: Boolean
    ): BlockArrayContainer<Block> {
        val seen = BooleanArray(4096)
        val id1Idx = Ref.IntRef()
        val id2Idx = Ref.IntRef()
        val metaIdx = Ref.IntRef()
        var unique = 0
        for (x in 0..16) {
            for (z in 0..16) {
                for (y in 0..8) {
                    getIndex(x, y, z, id1Idx, id2Idx, metaIdx, extraArg)
                    val metaByte = metaArray[metaIdx.element].code
                    val id1 = idArray[id1Idx.element].code shl 4 or (metaByte and 0xf)
                    val id2 = idArray[id2Idx.element].code shl 4 or ((metaByte shr 4) or 0xf)
                    if (!seen[id1]) {
                        seen[id1] = true
                        unique++
                    }
                    if (!seen[id2]) {
                        seen[id2] = true
                        unique++
                    }
                }
            }
        }
        val result = BlockArrayContainer<Block>(unique)
        var rX: Int
        var rZ: Int
        var rY: Int
        for (x in 0..16) {
            for (z in 0..16) {
                for (y in 0..8) {
                    getIndex(x, y, z, id1Idx, id2Idx, metaIdx, extraArg)
                    val metaByte = metaArray[metaIdx.element].code
                    if (isYZX) {
                        rX = y shl 1
                        rY = x
                        rZ = z
                    } else {
                        rX = x
                        rY = y shl 1
                        rZ = z
                    }
                    result.set(x, y shl 1, z, mapper(idArray[id1Idx.element].code, metaByte and 0xf))
                    if (isYZX) {
                        rX = y shl 1 or 1
                        rY = x
                        rZ = z
                    } else {
                        rX = x
                        rY = y shl 1 or 1
                        rZ = z
                    }
                    result.set(rX, rY, rZ, mapper(idArray[id2Idx.element].code, metaByte shr 4 and 0xf))
                }
            }
        }
        return result
    }
}
