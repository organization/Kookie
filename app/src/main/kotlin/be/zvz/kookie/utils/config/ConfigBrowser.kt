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
    fun parse(json: String?): ConfigBrowser {
        return create(mapper.readTree(json))
    }

    /**
     * Parse from string.
     * @param stream The JSON object as a stream
     * @return JsonBrowser instance for navigating in the result
     * @throws IOException When parsing the JSON failed
     */
    @Throws(IOException::class)
    fun parse(stream: InputStream?): ConfigBrowser {
        return create(mapper.readTree(stream))
    }

    @Throws(IOException::class)
    fun newMap(): ConfigBrowser {
        return create(mapper.createObjectNode())
    }

    @Throws(IllegalArgumentException::class)
    fun <K, V> toMap(): Map<K, V> {
        return mapper.convertValue(node, object : TypeReference<Map<K, V>>() {})
    }

    /**
     * @return True if the value represents a list.
     */
    val isList: Boolean
        get() = node is ArrayNode

    /**
     * @return True if the value represents a map.
     */
    val isMap: Boolean
        get() = node is ObjectNode

    /**
     * Get an element at an index for a list value
     * @param index List index
     * @return JsonBrowser instance which wraps the value at the specified index
     */
    fun index(index: Int): ConfigBrowser {
        return if (node !== null && isList && index >= 0 && index < node.size()) {
            create(node.get(index))
        } else {
            NULL_BROWSER
        }
    }

    /**
     * Get an element by key from a map value
     * @param key Map key
     * @return JsonBrowser instance which wraps the value with the specified key
     */
    operator fun get(key: String?): ConfigBrowser {
        return if (node !== null && isMap) {
            create(node.get(key))
        } else {
            NULL_BROWSER
        }
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

    fun exists(key: String): Boolean {
        if (node is ObjectNode) {
            return node.findValue(key) !== null
        } else {
            throw IllegalStateException("Exists only works on a map")
        }
    }

    fun remove(key: String): Boolean {
        if (node is ObjectNode) {
            return node.remove(key) !== null
        } else {
            throw IllegalStateException("Remove only works on a map")
        }
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
        node?.fieldNames()?.forEachRemaining {
            add(it)
        }
    }

    /**
     * Attempt to retrieve the value in the specified format
     * @param klass The class to retrieve the value as
     * @return The value as an instance of the specified class
     * @throws IllegalArgumentException If conversion is impossible
     */
    fun <T> `as`(klass: Class<T>?): T {
        return try {
            mapper.treeToValue(node, klass)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun <T> `as`(type: TypeReference<T>?): T {
        return try {
            mapper.readValue(mapper.treeAsTokens(node), type)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    /**
     * @return The value of the element as text
     */
    fun text(): String? {
        return if (node !== null) {
            when {
                node.isNull -> {
                    null
                }
                node.isTextual -> {
                    node.textValue()
                }
                node.isIntegralNumber -> {
                    node.longValue().toString()
                }
                node.isNumber -> {
                    node.numberValue().toString()
                }
                node.isBoolean -> {
                    node.booleanValue().toString()
                }
                else -> {
                    node.toString()
                }
            }
        } else null
    }

    fun asBoolean(defaultValue: Boolean): Boolean {
        if (node !== null) {
            if (node.isBoolean) {
                return node.booleanValue()
            } else if (node.isTextual) {
                return when (node.textValue().lowercase()) {
                    "true", "on", "1", "yes" -> true
                    else -> false
                }
            }
        }
        return defaultValue
    }

    fun asLong(defaultValue: Long): Long {
        if (node !== null) {
            if (node.isNumber) {
                return node.numberValue().toLong()
            } else if (node.isTextual) {
                try {
                    return node.textValue().toLong()
                } catch (ignored: NumberFormatException) {
                    // Fall through to default value.
                }
            }
        }
        return defaultValue
    }

    fun safeText(): String {
        val text = text()
        return text ?: ""
    }

    fun format(): String? {
        return try {
            if (node !== null) mapper.writeValueAsString(node) else null
        } catch (e: Exception) {
            null
        }
    }

    /**
     * @return The value of the element as text
     */
    val isNull: Boolean
        get() = node == null || node.isNull

    companion object {
        val NULL_BROWSER = NullBrowser.NULL_BROWSER
    }
}
