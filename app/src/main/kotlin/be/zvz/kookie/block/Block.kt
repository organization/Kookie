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

import be.zvz.kookie.world.Position

open class Block(val idInfo: BlockIdentifier, val name: String, val breakInfo: BlockBreakInfo) {
    val pos: Position = Position()

    init {
        // TODO: Variant collides with state bitmask
    }

    fun clone(): Block {
        return Block(idInfo, name, breakInfo)
    }

    fun getId(): Int = idInfo.blockId

    fun getMeta(): Int {
        val stateMeta = writeStateToMeta()
        // assert(($stateMeta & ~$this->getStateBitmask()) === 0);
        return idInfo.variant or stateMeta
    }

    fun getFullId(): Int = (getId() shl 4) or getMeta()

    private fun writeStateToMeta(): Int = 0
}
