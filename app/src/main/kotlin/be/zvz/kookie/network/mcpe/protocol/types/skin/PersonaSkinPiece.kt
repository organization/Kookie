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

class PersonaSkinPiece(
    private val pieceId: String,
    private val pieceType: String,
    private val packId: String,
    private val isDefaultPiece: Boolean,
    private val productId: String
) {

    fun getPieceId(): String = pieceId

    fun getPieceType(): String = pieceType

    fun getPackId(): String = packId

    fun isDefaultPiece(): Boolean = isDefaultPiece

    fun getProductId(): String = productId

    companion object {
        const val PIECE_TYPE_PERSONA_BODY = "persona_body"
        const val PIECE_TYPE_PERSONA_BOTTOM = "persona_bottom"
        const val PIECE_TYPE_PERSONA_EYES = "persona_eyes"
        const val PIECE_TYPE_PERSONA_FACIAL_HAIR = "persona_facial_hair"
        const val PIECE_TYPE_PERSONA_FEET = "persona_feet"
        const val PIECE_TYPE_PERSONA_HAIR = "persona_hair"
        const val PIECE_TYPE_PERSONA_MOUTH = "persona_mouth"
        const val PIECE_TYPE_PERSONA_SKELETON = "persona_skeleton"
        const val PIECE_TYPE_PERSONA_SKIN = "persona_skin"
        const val PIECE_TYPE_PERSONA_TOP = "persona_top"
    }
}