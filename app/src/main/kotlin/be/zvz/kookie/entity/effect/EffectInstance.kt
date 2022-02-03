/**
 *
 * _  __           _    _
 * | |/ /___   ___ | | _(_) ___
 * | ' // _ \ / _ \| |/ / |/ _ \
 * | . \ (_) | (_) |   <| |  __/
 * |_|\_\___/ \___/|_|\_\_|\___|
 *
 * A server software for Minecraft: Bedrock Edition
 *
 * Copyright (C) 2021 - 2022 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.entity.effect

import be.zvz.kookie.color.Color
import kotlin.math.max

class EffectInstance @JvmOverloads constructor(
    val effectType: Effect,
    duration: Int = effectType.defaultDuration,
    amplifier: Int = 0,
    val visible: Boolean = true,
    val ambient: Boolean = false,
    var color: Color = effectType.color
) {
    var duration: Int = duration
        set(value) {
            if (value !in 0..Int.MAX_VALUE) {
                throw IllegalArgumentException("Effect duration must be in range 0 - ${Int.MAX_VALUE}, got $value")
            }
            field = value
        }

    var amplifier: Int = amplifier
        set(value) {
            if (value !in 0..255) {
                throw IllegalArgumentException("Amplifier must be in range 0 - 255, got $value")
            }
            field = value
        }

    init {
        this.duration = duration.takeUnless { it < 0 } ?: effectType.defaultDuration
        // TODO: duration should be null on PMMP (if null, fill it with default duration)
    }

    val effectLevel: Int get() = amplifier + 1

    fun resetColor() {
        color = effectType.color
    }

    fun hasExpired(): Boolean = duration <= 0

    fun decreaseDuration(tickDiff: Int): EffectInstance = this.apply {
        duration = max(0, duration - tickDiff)
    }
}
