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

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

class NullBrowser private constructor(node: JsonNode?) : ConfigBrowser(node) {
    override val mapper: ObjectMapper
        get() = ObjectMapper()

    override fun create(node: JsonNode?): ConfigBrowser = NULL_BROWSER

    override fun put(key: String?, item: Any?) {}

    companion object {
        internal val NULL_BROWSER = NullBrowser(null)
    }
}
