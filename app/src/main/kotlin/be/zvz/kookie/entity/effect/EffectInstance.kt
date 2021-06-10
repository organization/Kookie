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
 * Copyright (C) 2021 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.entity.effect

import be.zvz.kookie.color.Color
import kotlin.math.max

data class EffectInstance(
    val effectType: Effect,
    var duration: Int = 0,
    var amplifier: Int = 0,
    var visible: Boolean = true,
    var ambient: Boolean = false,
    val overrideColor: Color? = null
) {

    var color: Color

    init {
        color = overrideColor ?: effectType.color
    }

    fun resetColor() {
        color = effectType.color
    }

    fun hasExpired(): Boolean = duration <= 0

    val effectLevel: Int = amplifier + 1

    fun decreaseDuration(tickDiff: Long): EffectInstance = this.apply {
        duration = max(0, duration.minus(tickDiff.toInt()))
    }
}
