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
package be.zvz.kookie.network.mcpe.protocol.types.skin

class SkinImage(private val height: Int, private val width: Int, private val data: String) {

    init {
        if (height < 0 || width < 0) {
            throw SkinImageException("height and width cannot be negative")
        }
        val expected = height * width * 4
        val actual = data.length
        if (expected != actual) {
            throw SkinImageException("Data should be exactly $expected bytes, got $actual bytes")
        }
    }

    fun getHeight(): Int = height

    fun getWidth(): Int = width

    fun getData(): String = data

    companion object {
        @JvmStatic
        fun fromLegacy(data: String) = when (data.length) {
            64 * 32 * 4 -> SkinImage(32, 64, data)
            64 * 64 * 4 -> SkinImage(64, 64, data)
            128 * 128 * 4 -> SkinImage(128, 128, data)
            else -> throw SkinImageException("Unknown size")
        }
    }

    class SkinImageException(message: String) : RuntimeException(message)
}
