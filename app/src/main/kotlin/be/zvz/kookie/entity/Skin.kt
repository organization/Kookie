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
package be.zvz.kookie.entity

class Skin @JvmOverloads constructor(
    val skinId: String,
    val skinData: String,
    val capeData: String = "",
    val geometryName: String = "",
    val geometryData: String = ""
) {

    init {
        if (skinId.trim() == "") {
            throw SkinException("Skin Id must not be empty")
        }
        val length = skinData.length
        if (!ACCEPTED_SKIN_SIZE.contains(length)) {
            throw SkinException(
                "Invalid skin data size $length bytes (allowed size: ${ACCEPTED_SKIN_SIZE.joinToString(", ")})"
            )
        }
        if (capeData != "" && capeData.length != 8192) {
            throw SkinException("Invalid cape data size ${capeData.length} (must be exactly 8192 bytes)")
        }
    }

    companion object {
        val ACCEPTED_SKIN_SIZE = listOf(
            64 * 32 * 4,
            64 * 64 * 4,
            128 * 128 * 4
        )
    }

    class SkinException(message: String) : RuntimeException(message)
}
