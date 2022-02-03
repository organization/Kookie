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
package be.zvz.kookie.network.mcpe.protocol.types.inventory

import be.zvz.kookie.nbt.tag.CompoundTag

data class ItemStack(
    val id: Int,
    val meta: Int,
    val count: Int,
    val blockRuntimeId: Int,
    val nbt: CompoundTag?,
    val canPlaceOn: List<String>,
    val canDestroy: List<String>,
    val shieldBlockingTick: Long?
) {
    companion object {
        @JvmStatic
        fun empty() = ItemStack(0, 0, 0, 0, null, listOf(), listOf(), null)
    }
}
