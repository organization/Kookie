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
package be.zvz.kookie.network.mcpe.protocol.types

data class MapTrackedObject @JvmOverloads constructor(
    var type: Int = 0,
    var entityUniqueId: Long = 0,
    var x: Int = 0,
    var y: Int = 0,
    var z: Int = 0
) {
    companion object {
        const val TYPE_ENTITY = 0
        const val TYPE_BLOCK = 1
    }
}
