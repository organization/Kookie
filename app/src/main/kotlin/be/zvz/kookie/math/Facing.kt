package be.zvz.kookie.math

class Facing {

    companion object {
        private const val FLAG_AXIS_POSITIVE = 1

        val DOWN = Axis.Y.value shl 1
        val UP = (Axis.Y.value shl 1) or FLAG_AXIS_POSITIVE
        val NORTH = Axis.Z.value shl 1
        val SOUTH = (Axis.Z.value shl 1) or FLAG_AXIS_POSITIVE
        val WEST = Axis.X.value shl 1
        val EAST = (Axis.X.value shl 1) or FLAG_AXIS_POSITIVE

        val ALL = arrayOf(DOWN, UP, NORTH, SOUTH, WEST, EAST)

        val HORIZONTAL = arrayOf(NORTH, SOUTH, WEST, EAST)

        val CLOCKWISE = mapOf(
            Axis.Y to mapOf(
                NORTH to EAST,
                EAST to SOUTH,
                SOUTH to WEST,
                WEST to NORTH
            ),
            Axis.Z to mapOf(
                UP to EAST,
                EAST to DOWN,
                DOWN to WEST,
                WEST to UP
            ),
            Axis.X to mapOf(
                UP to NORTH,
                NORTH to DOWN,
                DOWN to SOUTH,
                SOUTH to UP
            )
        )
    }
}