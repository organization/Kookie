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

class BlockIdentifierFlattened @JvmOverloads constructor(
    blockId: Int,
    val additionalIds: List<Int>,
    variant: Int,
    itemId: Int? = null,
    tileClass: String? = null
) : BlockIdentifier(blockId, variant, itemId, tileClass) {

    init {
        if (additionalIds.isEmpty()) {
            throw IllegalArgumentException("Expected at least 1 additional ID")
        }
    }

    fun getSecondId(): Int = additionalIds[0]

    override fun getAllBlockIds(): List<Int> {
        val list: MutableList<Int> = mutableListOf(blockId)
        additionalIds.forEach {
            list.add(it)
        }
        return list.toList()
    }
}
