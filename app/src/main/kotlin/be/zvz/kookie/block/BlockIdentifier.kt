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
package be.zvz.kookie.block

open class BlockIdentifier @JvmOverloads constructor(
    val blockId: Int,
    val variant: Int,
    itemId: Int? = null,
    val tileClass: String? = null
) {
    val itemId: Int? = itemId
        get() = field ?: if (blockId > 255) 255 - blockId else blockId

    open fun getAllBlockIds(): List<Int> = listOf(blockId)
}
