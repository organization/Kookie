package be.zvz.kookie.entity

class Attribute @JvmOverloads constructor(
    val id: String,
    minValue: Float,
    maxValue: Float,
    defaultValue: Float,
    private val shouldSend: Boolean = false
) {
    private var desynchronized: Boolean = false
    var minValue: Float = minValue
        set(value) {
            val max = maxValue
            if (value > max) {
                throw IllegalArgumentException("Minimum $value is greater than the maximum $max")
            }
            if (field != value) {
                desynchronized = true
                field = value
            }
        }
    var maxValue: Float = maxValue
        set(value) {
            val min = minValue
            if (value < min) {
                throw IllegalArgumentException("Maximum $value is less than the minimum $min")
            }
            if (field != value) {
                desynchronized = true
                field = value
            }
        }
    var defaultValue: Float = defaultValue
        set(value) {
            if (value > maxValue || value < minValue) {
                throw IllegalArgumentException("Default $defaultValue is outside the range $minValue - $maxValue")
            }
            if (field != defaultValue) {
                desynchronized = true
                field = value
            }
        }
    var currentValue: Float

    init {
        if (minValue > maxValue || defaultValue > maxValue || defaultValue < minValue) {
            throw IllegalArgumentException("Invalid ranges: min value: $minValue, max value: $maxValue, $defaultValue: $defaultValue")
        }
        currentValue = defaultValue
    }

    fun isSyncable(): Boolean = shouldSend
    fun isDesynchronized(): Boolean = shouldSend && desynchronized
    @JvmOverloads
    fun markSynchronized(synced: Boolean = true) {
        desynchronized = !synced
    }
}
