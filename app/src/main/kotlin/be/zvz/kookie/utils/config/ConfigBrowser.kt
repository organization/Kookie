/**
 * Copyright 2021 sedmelluq, JellyBrick. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
package be.zvz.kookie.utils.config

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import java.io.IOException
import java.io.InputStream

/**
 * Allows to easily navigate in decoded JSON data
 */
abstract class ConfigBrowser protected constructor(private val node: JsonNode?) {

    abstract val mapper: ObjectMapper

    abstract fun create(node: JsonNode?): ConfigBrowser

    /**
     * Parse from string.
     * @param json The JSON object as a string
     * @return JsonBrowser instance for navigating in the result
     * @throws IOException When parsing the JSON failed
     */
    @Throws(IOException::class)
    fun parse(json: String?): ConfigBrowser = create(mapper.readTree(json))

    /**
     * Parse from string.
     * @param stream The JSON object as a stream
     * @return JsonBrowser instance for navigating in the result
     * @throws IOException When parsing the JSON failed
     */
    @Throws(IOException::class)
    fun parse(stream: InputStream?): ConfigBrowser = create(mapper.readTree(stream))

    @Throws(IOException::class)
    fun newMap(): ConfigBrowser = create(mapper.createObjectNode())

    @Throws(IllegalArgumentException::class)
    fun <K, V> toMap(): Map<K, V> = mapper.convertValue(node, object : TypeReference<Map<K, V>>() {})

    /**
     * @return True if the value represents a list.
     */
    val isList: Boolean get() = node is ArrayNode

    /**
     * @return True if the value represents a map.
     */
    val isMap: Boolean get() = node is ObjectNode

    /**
     * Get an element at an index for a list value
     * @param index List index
     * @return JsonBrowser instance which wraps the value at the specified index
     */
    fun index(index: Int): ConfigBrowser =
        if (node !== null && isList && index in 0 until node.size()) {
            create(node.get(index))
        } else {
            NULL_BROWSER
        }

    /**
     * Get an element by key from a map value
     * @param key Map key
     * @return JsonBrowser instance which wraps the value with the specified key
     */
    operator fun get(key: String?): ConfigBrowser =
        if (node !== null && isMap) {
            create(node.get(key))
        } else {
            NULL_BROWSER
        }

    /**
     * Put a value into the map if this instance contains a map.
     * @param key The map entry key
     * @param item The map entry value
     */
    open fun put(key: String?, item: Any?) {
        if (node is ObjectNode) {
            if (item is ConfigBrowser) {
                node.set(key, item.node)
            } else {
                node.set(key, mapper.valueToTree(item))
            }
        } else {
            throw IllegalStateException("Put only works on a map")
        }
    }

    fun exists(key: String): Boolean =
        if (node is ObjectNode) {
            node.findValue(key) !== null
        } else {
            throw IllegalStateException("Exists only works on a map")
        }

    fun remove(key: String): Boolean =
        if (node is ObjectNode) {
            node.remove(key) !== null
        } else {
            throw IllegalStateException("Remove only works on a map")
        }

    /**
     * Returns a list of all the values in this element
     * @return The list of values as JsonBrowser elements
     */
    fun values(): List<ConfigBrowser> = mutableListOf<ConfigBrowser>().apply {
        node?.elements()?.forEachRemaining { child ->
            add(create(child))
        }
    }

    fun keys(): List<String> = mutableListOf<String>().apply {
        node?.fieldNames()?.forEachRemaining(this::add)
    }

    /**
     * Attempt to retrieve the value in the specified format
     * @param klass The class to retrieve the value as
     * @return The value as an instance of the specified class
     * @throws IllegalArgumentException If conversion is impossible
     */
    fun <T> `as`(klass: Class<T>?): T =
        try {
            mapper.treeToValue(node, klass)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    fun <T> `as`(type: TypeReference<T>?): T =
        try {
            mapper.readValue(mapper.treeAsTokens(node), type)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    fun text(): String? = node?.let {
        when {
            it.isNull -> null
            it.isTextual -> it.textValue()
            it.isIntegralNumber -> it.longValue().toString()
            it.isNumber -> it.numberValue().toString()
            it.isBoolean -> it.booleanValue().toString()
            else -> it.toString()
        }
    }

    fun asBoolean(defaultValue: Boolean): Boolean = node?.let {
        when {
            it.isBoolean -> it.booleanValue()
            it.isTextual -> when (it.textValue().lowercase()) {
                "true", "on", "1", "yes" -> true
                else -> false
            }
            else -> null
        }
    } ?: defaultValue

    fun asLong(defaultValue: Long): Long = node?.let {
        when {
            it.isNumber -> it.numberValue().toLong()
            it.isTextual -> try {
                node.textValue().toLong()
            } catch (ignored: NumberFormatException) {
                null // Fall through to default value.
            }
            else -> null
        }
    } ?: defaultValue

    fun safeText(): String = text() ?: ""

    fun format(): String? = node?.let {
        try {
            mapper.writeValueAsString(node)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * @return The value of the element as text
     */
    val isNull: Boolean get() = node == null || node.isNull

    companion object {
        val NULL_BROWSER = NullBrowser.NULL_BROWSER
    }
}
