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

class PersonaPieceTintColor(private val pieceType: String, private val colors: MutableList<String>) {

    fun getPieceType(): String = pieceType

    fun getColors(): MutableList<String> = colors

    companion object {
        const val PIECE_TYPE_PERSONA_EYES = "persona_eyes"
        const val PIECE_TYPE_PERSONA_HAIR = "persona_hair"
        const val PIECE_TYPE_PERSONA_MOUTH = "persona_mouth"
    }
}
