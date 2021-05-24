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

object BlockFactory {
    private val fullList = mutableListOf<Block>()

    fun fromFullBlock(fullState: Int): Block = get(fullState shr 4, fullState and 0xf)

    fun get(id: Int, meta: Int): Block {
        if (meta !in 0..0xf) {
            throw IllegalArgumentException("Block meta value $meta is out of bounds")
        }

        return try {
            val index = (id shl 4) or meta

            fullList[index].clone()
        } catch (err: RuntimeException) {
            throw IllegalArgumentException("Block ID $id is out of bounds")
        }
    }
}
