package be.zvz.kookie.math

enum class Facing(val value: Int) {

    DOWN(Axis.Y.value shl 1),
    UP(Axis.Y.value shl 1 or Facing.FLAG_AXIS_POSITIVE),
    NORTH(Axis.Z.value shl 1),
    SOUTH(Axis.Z.value shl 1 or Facing.FLAG_AXIS_POSITIVE),
    WEST(Axis.X.value shl 1),
    EAST(Axis.X.value shl 1 or Facing.FLAG_AXIS_POSITIVE);

    companion object {
        private const val FLAG_AXIS_POSITIVE = 1
        val ALL = arrayOf(
            DOWN,
            UP,
            NORTH,
            SOUTH,
            WEST,
            EAST
        )

        val HORIZONTAL = arrayOf(
            NORTH,
            SOUTH,
            WEST,
            EAST
        )

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
        @JvmStatic
        fun axis(direction: Int): Int = direction shr 1

        @JvmStatic
        fun isPositive(direction: Int): Boolean = (direction and FLAG_AXIS_POSITIVE) == FLAG_AXIS_POSITIVE

        @JvmStatic
        fun opposite(direction: Int): Int = direction xor FLAG_AXIS_POSITIVE

        @JvmStatic
        fun rotate(direction: Facing, axis: Axis, clockWise: Boolean): Int {
            if (!CLOCKWISE.containsKey(axis)) {
                throw RuntimeException("Invalid axis ${direction.value}")
            }
            if (!CLOCKWISE.getValue(axis).containsKey(direction)) {
                throw RuntimeException("Cannot rotate direction ${direction.value} around axis ${axis.value}")
            }
            val rotated = CLOCKWISE.getValue(axis).getValue(direction)
            return if (clockWise) {
                rotated.value
            } else {
                opposite(rotated.value)
            }
        }
    }
}