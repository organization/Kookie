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
package be.zvz.kookie.utils

import org.apache.commons.io.IOUtils
import java.net.URL

object Internet {
    var ip = ""
    var online = true

    @JvmStatic
    @JvmOverloads
    fun getIP(force: Boolean = false): String {
        if (!online) {
            return ""
        } else if (ip.isNotEmpty() && !force) {
            return ip
        }

        val connection = URL("https://checkip.amazonaws.com")
            .openConnection()
        connection.getInputStream().use {
            val encoding = connection.contentEncoding ?: "UTF-8"
            return IOUtils.toString(it, encoding).trim()
        }
    }
}
