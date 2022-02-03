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
package be.zvz.kookie.permission

import java.io.File

class BanList(val file: File) {

    var enabled: Boolean = true
        private set

    val list: MutableList<BanEntry> = mutableListOf()
        get() {
            removeExpired()
            return field
        }

    fun removeExpired() {
        list.apply {
            val iterator = listIterator()
            while (iterator.hasNext()) {
                val current = iterator.next()
                if (current.hasExpired()) {
                    iterator.remove()
                }
            }
        }
    }
}
