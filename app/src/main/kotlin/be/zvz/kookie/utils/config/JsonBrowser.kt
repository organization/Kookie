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
package be.zvz.kookie.utils.config

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

class JsonBrowser private constructor(node: JsonNode?) : ConfigBrowser(node) {
    override val mapper = staticMapper

    internal constructor() : this(staticMapper.createObjectNode())

    override fun create(node: JsonNode?): ConfigBrowser = JsonBrowser(node)

    companion object {
        private val staticMapper = ObjectMapper(
            JsonFactory().apply {
                enable(JsonParser.Feature.ALLOW_COMMENTS)
                enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
            }
        )
    }
}
