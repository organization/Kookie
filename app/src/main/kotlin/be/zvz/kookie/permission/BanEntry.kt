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
package be.zvz.kookie.permission

import java.text.SimpleDateFormat
import java.util.Date

data class BanEntry(
    var name: String
) {
    var creationTime: Date = Date()
    var source: String = "(Unknown)"
    var expirationDate: Date? = null
    var reason: String = "Banned by operator"

    fun hasExpired(): Boolean = expirationDate?.let {
        Date().time > it.time
    } ?: false

    override fun toString(): String = "$name|$creationTime|$source|${expirationDate?.let {
        DATETIME_FORMAT.format(it)
    } ?: "Forever"}|$reason"

    companion object {
        private val DATETIME_FORMAT = SimpleDateFormat("Y-m-d H:i:s O")

        fun fromString(str: String): BanEntry? {
            if (str.length < 2) {
                return null
            }
            val parts = str.split("|", str.trim()).toMutableList()
            val entry = BanEntry(parts.removeFirst().trim())
            if (parts.size > 0) {
                entry.creationTime = DATETIME_FORMAT.parse(parts.removeFirst().trim())
            }
            if (parts.size > 0) {
                entry.source = parts.removeFirst().trim()
            }
            if (parts.size > 0) {
                val expire = parts.removeFirst().trim()
                if (expire.isNotEmpty() && "forever".equals(expire, true)) {
                    entry.expirationDate = DATETIME_FORMAT.parse(expire)
                }
            }
            if (parts.size > 0) {
                entry.reason = parts.removeFirst().trim()
            }
            return entry
        }
    }
}
