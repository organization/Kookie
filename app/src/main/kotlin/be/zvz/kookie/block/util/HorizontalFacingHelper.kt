package be.zvz.kookie.block.util

import be.zvz.kookie.math.Axis
import be.zvz.kookie.math.Facing

interface HorizontalFacingHelper {
    var facing: Int
        get() = Facing.NORTH.value
        set(value) {
            val axis = Facing.axis(value)
            if(axis != Axis.X.value && axis != Axis.Z.value) {
                throw IllegalArgumentException("Facing must be horizontal")
            }
            //TODO: need review (return & recursive access)
            facing = value
        }
}
