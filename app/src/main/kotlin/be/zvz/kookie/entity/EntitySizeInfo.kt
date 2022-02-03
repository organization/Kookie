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
package be.zvz.kookie.entity

import kotlin.math.min

data class EntitySizeInfo @JvmOverloads constructor(
    val height: Float,
    val width: Float,
    var eyeHeight: Float? = null
) {
    init {
        eyeHeight = eyeHeight ?: min(height / 2 + 0.1F, height)
    }

    fun scale(newScale: Float): EntitySizeInfo {
        return EntitySizeInfo(height * newScale, width * newScale, eyeHeight!! * newScale)
    }
}
