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
package be.zvz.kookie.network.mcpe.protocol.resourcepacks

enum class ResourcePackType(val value: Int) {
    INVALID(0),
    ADDON(1),
    CACHED(2),
    COPY_PROTECTED(3),
    BEHAVIORS(4),
    PERSONA_PIECE(5),
    RESOURCES(6),
    SKINS(7),
    WORLD_TEMPLATE(8);

    companion object {
        private val VALUES = values()
        @JvmStatic
        fun from(findValue: Int) = VALUES.first { it.value == findValue }
    }
}
