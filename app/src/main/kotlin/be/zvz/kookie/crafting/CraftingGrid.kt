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
package be.zvz.kookie.crafting

import be.zvz.kookie.inventory.SimpleInventory
import be.zvz.kookie.item.Item
import be.zvz.kookie.player.Player
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

class CraftingGrid(val holder: Player, val gridWidth: Int) : SimpleInventory(gridWidth.toDouble().pow(2).toInt()) {

    private var startX: Int? = null
    private var xLen: Int? = null

    private var startY: Int? = null
    private var yLen: Int? = null
    override fun setItem(index: Int, item: Item) {
        super.setItem(index, item)
    }

    private fun seekRecipeBounds() {
        var minX = Int.MAX_VALUE
        var maxX = 0
        var minY = Int.MAX_VALUE
        var maxY = 0

        var empty = true

        repeat(gridWidth) { x ->
            repeat(gridWidth) { y ->
                if (!isSlotEmpty(y * gridWidth + x)) {
                    minX = min(minX, x)
                    maxX = max(maxX, x)

                    minY = min(minY, y)
                    maxY = max(maxY, y)

                    empty = false
                }
            }
        }
        if (!empty) {
            startX = minX
            xLen = maxX - minX + 1
            startY = minY
            yLen = maxY - minY + 1
        } else {
            startX = null
            xLen = null
            startY = null
            yLen = null
        }
    }

    fun getIngredient(x: Int, y: Int): Item {
        if (startX != null && startY != null) {
            return getItem((y + startY!!) * gridWidth + (x + startX!!))
        }
        throw IllegalStateException("No ingredients found in grid")
    }

    fun getRecipeWidth(): Int = xLen ?: 0

    fun getRecipeHeight(): Int = yLen ?: 0

    companion object {
        const val SIZE_SMALL = 2
        const val SIZE_BIG = 3
    }
}
