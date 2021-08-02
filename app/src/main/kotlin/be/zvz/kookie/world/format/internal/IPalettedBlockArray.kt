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
package be.zvz.kookie.world.format.internal

interface IPalettedBlockArray<Block> : Cloneable {
    val wordArray: CharArray
    val palette: List<Block>
    val paletteSize: Int
    val maxPaletteSize: Int
    val bitsPerBlock: Int

    fun get(x: Int, y: Int, z: Int): Block
    fun set(x: Int, y: Int, z: Int, v: Block): Boolean
    fun getPaletteOffset(x: Int, y: Int, z: Int): Int
    fun replace(offset: Int, value: Block)
    fun replaceAll(from: Block, to: Block)
    fun convertFrom(otherArray: IPalettedBlockArray<Block>)
    fun fastUpsize(otherArray: IPalettedBlockArray<Block>)
    public override fun clone(): IPalettedBlockArray<*> = super.clone() as IPalettedBlockArray<*>

    companion object {
        const val COORD_BIT_SIZE = 4
        const val ARRAY_DIM = 1 shl COORD_BIT_SIZE
        const val ARRAY_CAPACITY = ARRAY_DIM * ARRAY_DIM * ARRAY_DIM
    }
}
