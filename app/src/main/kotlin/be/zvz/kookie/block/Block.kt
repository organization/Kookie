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

import be.zvz.kookie.item.Item
import be.zvz.kookie.item.ItemFactory
import be.zvz.kookie.math.AxisAlignedBB
import be.zvz.kookie.world.Position
import java.lang.IllegalArgumentException

open class Block(val idInfo: BlockIdentifier, val name: String, val breakInfo: BlockBreakInfo) {
    var pos: Position

    protected var collisionBoxes: MutableList<AxisAlignedBB>? = null

    fun dechex(ig: Int) = 0

    init {
        // TODO: Variant collides with state bitmask
        if((idInfo.variant and getStateBitmask()) != 0){
            throw IllegalArgumentException("Variant 0x" + dechex(idInfo.variant) + " collides with state bitmask 0x" + dechex(getStateBitmask()))
        }
        pos = Position(0f, 0f, 0f, null)
    }

    fun clone(): Block {
        return Block(idInfo, name, breakInfo)
    }

    fun getId(): Int = idInfo.blockId

    fun getFullId(): Int = (getId() shl 4) or getMeta()

    fun asItem(): Item = ItemFactory.get(
        idInfo.itemId!!,
        idInfo.variant or (writeStateToMeta() and getNonPersistentStateBitmask().inv())
    )

    fun getMeta(): Int {
        val stateMeta = writeStateToMeta()
        assert((stateMeta and getStateBitmask().inv()) == 0);
        return idInfo.variant or stateMeta
    }

    open fun getStateBitmask(): Int = 0

    open fun getNonPersistentStateBitmask(): Int = 0

    protected fun writeStateToMeta(): Int = 0

    open fun readStateFromData(id: Int, stateMeta: Int) {
        // NOOP
    }

    open fun readStateFromWorld() {
        collisionBoxes = null
    }

    open fun canBePlaced(): Boolean = true
}
