package be.zvz.kookie.block.utils

import be.zvz.kookie.math.Axis
import be.zvz.kookie.math.Facing

interface HorizontalFacing {
    companion object {
        var facing: Facing = Facing.NORTH
            get() = Facing.NORTH
            set(value) {
                val axis = Facing.axis(value.value)
                if (axis != Axis.X.value && axis != Axis.Z.value) {
                    throw IllegalArgumentException("Facing must be horizontal")
                }
                //TODO: need review (if return value is needed)
                field = value
            }
    }
}
