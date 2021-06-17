package be.zvz.kookie.math

import java.util.Random
import kotlin.math.pow
import kotlin.math.sqrt

object Math {

    @JvmStatic
    fun solveQuadratic(a: Float, b: Float, c: Float): List<Float> {
        val discriminant = (b.toDouble().pow(2) - 4 * a * c).toFloat()
        return if (discriminant > 0) {
            val sqrtDiscriminant = sqrt(discriminant)
            listOf(
                (-b + sqrtDiscriminant) / (2 * a),
                (-b - sqrtDiscriminant) / (2 * a)
            )
        } else if (discriminant == 0F) {
            listOf(
                -b / (2 * a)
            )
        } else {
            listOf()
        }
    }

    @JvmStatic
    fun maxFromList(list: List<Float>): Float {
        var max = Float.MIN_VALUE
        list.forEach {
            if (it > max) {
                max = it
            }
        }
        return max
    }

    @JvmStatic
    fun random(min: Int, max: Int): Int {
        val rand = Random()
        return rand.nextInt(max - min + 1) + min
    }
}
