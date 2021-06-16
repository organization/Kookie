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
package be.zvz.kookie.item

class ItemIdentifier(val id: Int, meta: Int) {
    val meta: Int

    init {
        if (id !in -0x8000..0x7fff) {
            throw IllegalArgumentException("ID must be in range ${-0x8000} - ${0x7fff}")
        }

        this.meta = if (meta != -1) meta and 0x7FFF else -1
    }
}
