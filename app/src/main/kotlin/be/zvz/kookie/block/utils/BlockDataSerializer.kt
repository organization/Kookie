package be.zvz.kookie.block.utils

import be.zvz.kookie.math.Axis
import be.zvz.kookie.math.Facing

class BlockDataSerializer {
    companion object {
        fun readFacing(raw: Int): Facing = when (raw) {
            0 -> Facing.DOWN
            1 -> Facing.UP
            2 -> Facing.NORTH
            3 -> Facing.SOUTH
            4 -> Facing.WEST
            5 -> Facing.EAST
            else -> throw IllegalArgumentException("Invalid facing $raw")
        }

        fun writeFacing(facing: Facing): Int = when (facing) {
            Facing.DOWN -> 0
            Facing.UP -> 1
            Facing.NORTH -> 2
            Facing.SOUTH -> 3
            Facing.WEST -> 4
            Facing.EAST -> 5
            else -> throw IllegalArgumentException("Invalid facing $facing")
        }

        fun readHorizontalFacing(facing: Int): Facing =
            readFacing(facing).let {
                return if (it.value == Axis.Y.value) throw IllegalArgumentException("Invalid Y-axis facing $facing")
                else it
            }

        fun writeHorizontalFacing(facing: Facing): Int =
            if (facing.value == Axis.Y.value) throw IllegalArgumentException("Invalid Y-axis facing")
            else writeFacing(facing)

        fun readLegacyHorizontalFacing(raw: Int): Facing = when (raw) {
            0 -> Facing.SOUTH
            1 -> Facing.WEST
            2 -> Facing.NORTH
            3 -> Facing.EAST
            else -> throw IllegalArgumentException("Invalid legacy facing $raw")
        }

        fun writeLegacyHorizontalFacing(facing: Facing): Int = when (facing) {
            Facing.SOUTH -> 0
            Facing.WEST -> 1
            Facing.NORTH -> 2
            Facing.EAST -> 3
            else -> throw IllegalArgumentException("Invalid Y-axis facing")
        }

        fun read5MinusHorizontalFacing(value: Int): Facing = readHorizontalFacing(5 - (value and 0x03))
        fun write5MinusHorizontalFacing(value: Facing): Int = 5 - writeHorizontalFacing(value)

        fun readCoralFacing(value: Int): Facing = when (value) {
            0 -> Facing.WEST
            1 -> Facing.EAST
            2 -> Facing.NORTH
            3 -> Facing.SOUTH
            else -> throw IllegalArgumentException("Invalid coral facing $value")
        }

        fun writeCoralFacing(value: Facing): Int = when (value) {
            Facing.WEST -> 0
            Facing.EAST -> 1
            Facing.NORTH -> 2
            Facing.SOUTH -> 3
            else -> throw IllegalArgumentException("Invalid Y-axis facing $value")
        }

        fun readBoundedInt(name: String, v: Int, min: Int, max: Int): Int =
            if (v < min || v > max) throw IllegalArgumentException("$name should be in range $min - $max, got $v")
            else v
    }
}
