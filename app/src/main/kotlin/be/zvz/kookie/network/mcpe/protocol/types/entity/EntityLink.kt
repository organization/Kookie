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
package be.zvz.kookie.network.mcpe.protocol.types.entity

class EntityLink(
    val fromEntityId: Long,
    val toEntityId: Long,
    val type: Int,
    val immediate: Boolean,
    val causedByRider: Boolean
) {

    companion object {
        const val TYPE_REMOVE = 0
        const val TYPE_RIDER = 1
        const val TYPE_PASSENGER = 2
    }
}
