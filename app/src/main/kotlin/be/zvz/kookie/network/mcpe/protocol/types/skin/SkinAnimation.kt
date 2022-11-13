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
package be.zvz.kookie.network.mcpe.protocol.types.skin

class SkinAnimation(
    private val image: SkinImage,
    private val type: Int,
    private val frames: Float,
    private val expressionType: Int
) {

    fun getImage(): SkinImage = image

    fun getType(): Int = type

    fun getFrames(): Float = frames

    fun getExpressionType(): Int = expressionType

    companion object {
        const val TYPE_HEAD = 1
        const val TYPE_BODY_32 = 2
        const val TYPE_BODY_64 = 3

        const val EXPRESSION_LINEAR = 0 // ???
        const val EXPRESSION_BLINKING = 1
    }
}
