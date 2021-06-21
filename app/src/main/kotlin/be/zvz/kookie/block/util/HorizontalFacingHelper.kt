package be.zvz.kookie.block.util

import be.zvz.kookie.math.Facing

interface HorizontalFacingHelper {
    val facing: Facing
        get() = Facing.NORTH
}
