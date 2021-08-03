package be.zvz.kookie.block.utils

import be.zvz.kookie.math.Axis
import be.zvz.kookie.math.Facing
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface HorizontalFacing {
    var facing: Facing

    companion object {
        internal inline fun observer(): ReadWriteProperty<Any?, Facing> =
            object : ObservableProperty<Facing>(Facing.NORTH) {
                override fun afterChange(property: KProperty<*>, oldValue: Facing, newValue: Facing) {
                    val axis = Facing.axis(newValue)
                    if (axis != Axis.X.value && axis != Axis.Z.value) {
                        throw IllegalArgumentException("Facing must be horizontal")
                    }
                }
            }
    }
}
