package be.zvz.kookie.world.format

class SubChunk(
    val emptyBlockId: Long,
    var blockLayers: MutableList<PalettedBlockArray>,
    var skyLight: LightArray = LightArray.fill(0),
    var blockLight: LightArray = LightArray.fill(0),
) {

    fun isEmptyAuthoritative(): Boolean {
        collectGarbage()

        return isEmptyFast()
    }

    fun isEmptyFast(): Boolean = blockLayers.size == 0

    fun getFullBlock(x: Int, y: Int, z: Int): Long = when (blockLayers.size) {
        0 -> emptyBlockId
        else -> blockLayers[0].get(x, y, z)
    }

    fun setFullBlock(x: Int, y: Int, z: Int, block: Long) {
        when (blockLayers.size) {
            0 -> blockLayers.add(PalettedBlockArray(emptyBlockId))
            else -> blockLayers[0].set(x, y, z, block)
        }
    }

    fun getHighestBlockAt(x: Int, z: Int): Int? {
        if (blockLayers.size == 0) {
            return null
        }

        for (y in 15 downTo 0) {
            if (blockLayers[0].get(x, y, z) != emptyBlockId) {
                return y
            }
        }

        return null
    }

    fun collectGarbage() {
        var check: Boolean
        blockLayers.forEachIndexed { index, layer ->
            check = false
            // TODO: layer.collectGarbage()

            layer.getPalette().forEach {
                if (it != emptyBlockId) {
                    check = true
                    return@forEach
                }
            }

            if (!check) {
                blockLayers.removeAt(index)
            }
        }

        // TODO: skyLight.collectGarbage()
        // TODO: blockLight.collectGarbage()
    }

    fun clone(): SubChunk {
        return SubChunk(
            emptyBlockId,
            blockLayers,
            skyLight.clone(),
            blockLight.clone()
        )
    }
}
