package be.zvz.kookie.entity.effect

import be.zvz.kookie.color.Color
import kotlin.math.max

class EffectInstance(val effectType: Effect, duration: Int = effectType.defaultDuration, amplifier: Int = 0, val visible: Boolean = true, val ambient: Boolean = false, val overrideColor: Color = effectType.color) {

    var duration: Int = duration
        set(value) {
            if (value < 0 || value > Int.MAX_VALUE) {
                throw IllegalArgumentException("Effect duration must be in range 0 - ${Int.MAX_VALUE}, got $value")
            }
            field = value
        }

    var amplifier: Int = amplifier
        set(value) {
            if (value < 0 || value > 255) {
                throw IllegalArgumentException("Amplifier must be in range 0 - 255, got $value")
            }
            field = value
        }

    fun decreaseDuration(ticks: Int): EffectInstance = this.apply {
        duration = max(0, duration - ticks)
    }

    fun hasExpired(): Boolean = duration <= 0

    fun getEffectLevel(): Int = amplifier + 1
}
