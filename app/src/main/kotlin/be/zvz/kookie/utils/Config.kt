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

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.inputStream

class Config(private val file: String, private var type: Type = Type.DETECT, default: ConfigBrowser = ConfigBrowser.NULL_BROWSER) {
    val path: Path = Paths.get(file)

    enum class Type {
        DETECT,
        PROPERTIES,
        JSON,
        YAML,
        ENUM,
        ERROR
    }
    private var changed = false
    private var config: ConfigBrowser = ConfigBrowser.NULL_BROWSER

    init {
        load(default)
    }

    private fun load(default: ConfigBrowser) {
        if (type === Type.DETECT) {
            val extension = FilenameUtils.getExtension(file)
            val format = getFormat(extension)
            if (format !== Type.ERROR) {
                type = format
            } else {
                throw IllegalArgumentException("Cannot detect config type of $file")
            }
        }
        if (!path.exists()) {
            config = default
            save()
        } else {
            config = path.inputStream().use { inputStream ->
                BufferedInputStream(inputStream).use {
                    when (type) {
                        Type.PROPERTIES -> PropertiesBrowser().parse(it)
                        Type.JSON -> JsonBrowser().parse(it)
                        Type.YAML -> YAMLBrowser().parse(it)
                        Type.ENUM -> parseList(it)
                        else -> throw IllegalStateException("Config type is unknown")
                    }
                }
            }
            if (fillDefault(default, config) > 0) {
                save()
            }
        }
    }

    @JvmOverloads
    fun get(key: String, default: ConfigBrowser = ConfigBrowser.NULL_BROWSER): ConfigBrowser {
        val value = config[key]
        return if (value !== ConfigBrowser.NULL_BROWSER) {
            value
        } else {
            default
        }
    }

    fun set(key: String, value: Any) {
        config.put(key, value)
        changed = true
    }

    fun set(key: String, value: ConfigBrowser) {
        config.put(key, value)
        changed = true
    }

    fun getAll(): ConfigBrowser = config

    fun setAll(browser: ConfigBrowser) {
        config = browser
        changed = true
    }

    fun setDefaults(default: ConfigBrowser) {
        fillDefault(default, config)
    }

    fun exists(key: String): Boolean = config.exists(key)

    fun remove(key: String): Boolean = config.remove(key)

    fun save() {
        val content = when (type) {
            Type.PROPERTIES -> config.safeText()
            Type.JSON -> config.safeText()
            Type.YAML -> config.safeText()
            Type.ENUM -> config.keys().joinToString("\r\n")
            else -> throw IllegalStateException("Config type is unknown, has not been set or not detected")
        }

        FileUtils.writeStringToFile(path.toFile(), content, StandardCharsets.UTF_8)
    }

    private fun fillDefault(default: ConfigBrowser, data: ConfigBrowser): Int {
        var changed = 0
        default.keys().forEach {
            if (data[it] === ConfigBrowser.NULL_BROWSER) {
                data.put(it, default[it])
                ++changed
            }
        }

        if (changed > 0) {
            this.changed = true
        }
        return changed
    }

    private fun parseList(inputStream: InputStream): ConfigBrowser = JsonBrowser().apply {
        inputStream.bufferedReader().use(BufferedReader::readText)
            .replace("\r\n", "\n").trim().split('\n')
            .forEach {
                val v = it.trim()
                if (v.isEmpty()) {
                    return@forEach
                }
                put(v, true)
            }
    }

    companion object {
        fun getFormat(type: String) = when (type) {
            "properties", "cnf", "conf", "config" -> Type.PROPERTIES
            "json", "js" -> Type.JSON
            "yml", "yaml" -> Type.YAML
            // "export", "xport" -> EXPORT
            // "sl", "serialize" -> SERIALIZED
            "txt", "list", "enum" -> Type.ENUM
            else -> Type.ERROR
        }
    }
}
