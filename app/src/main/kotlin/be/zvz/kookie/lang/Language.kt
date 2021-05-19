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
package be.zvz.kookie.lang

import be.zvz.kookie.constant.CorePaths
import com.koloboke.collect.map.hash.HashObjObjMaps
import org.apache.commons.io.FilenameUtils
import org.ini4j.Ini
import org.ini4j.IniPreferences
import java.nio.file.Path
import java.nio.file.Paths
import java.util.prefs.Preferences
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries

class Language(langStr: String, path: String? = null, fallback: String = FALLBACK_LANGUAGE) {
    private val langName: String = langStr.lowercase()
    private val languagePrefs: Preferences
    private val fallbackLang: Preferences

    init {
        val pathObj = if (path === null) {
            CorePaths.RESOURCE_PATH
        } else {
            Paths.get(path)
        }

        languagePrefs = loadLanguage(pathObj)
        fallbackLang = loadLanguage(pathObj)
    }

    val name: String
        get() = languagePrefs.get("language.name", null)
    val lang: String
        get() = langName

    private fun getBaseText(str: String, onlyPrefix: String? = null): String = get(str).apply {
        parseTranslation(
            if (onlyPrefix !== null && str.indexOf(onlyPrefix) == 0) {
                this
            } else {
                str
            },
            onlyPrefix
        )
    }

    @JvmOverloads
    fun translateString(
        str: String,
        onlyPrefix: String? = null
    ): String = getBaseText(str, onlyPrefix)

    @JvmOverloads
    fun translateString(
        str: String,
        params: List<String>,
        onlyPrefix: String? = null
    ): String {
        var baseText = getBaseText(str, onlyPrefix)
        params.forEachIndexed { index, value ->
            baseText = baseText.replace("{%$index}", parseTranslation(value))
        }

        return baseText
    }

    @JvmOverloads
    fun translateString(
        str: String,
        params: Map<String, String>,
        onlyPrefix: String? = null
    ): String {
        var baseText = getBaseText(str, onlyPrefix)
        params.forEach { (key, value) ->
            baseText = baseText.replace("{%$key}", parseTranslation(value))
        }

        return baseText
    }

    private fun internalGet(id: String): String? = languagePrefs.get(id, fallbackLang.get(id, null))
    fun get(id: String): String = internalGet(id) ?: id

    fun parseTranslation(text: String, onlyPrefix: String? = null): String {
        val newStringBuilder = StringBuilder()
        val replaceString = StringBuilder()
        text.forEach {
            if (replaceString.isNotEmpty()) {
                val ord = it.code
                if (
                    ord in 0x30..0x39 || // 0-9
                    ord in 0x41..0x5a || // A-Z
                    ord in 0x61..0x7a || // a-z
                    it == '.' || it == '-'
                ) {
                    replaceString.append(it)
                } else {
                    val t = internalGet(replaceString.substring(1))
                    if (t !== null && onlyPrefix !== null || replaceString.indexOf(onlyPrefix) == 1) {
                        newStringBuilder.append(t)
                    } else {
                        newStringBuilder.append(replaceString)
                    }
                    replaceString.clear()
                }
            } else if (it == '%') {
                replaceString.append(it)
            } else {
                newStringBuilder.append(it)
            }
        }

        if (replaceString.isNotEmpty()) {
            val t = internalGet(replaceString.substring(1))
            if (t !== null && onlyPrefix !== null || replaceString.indexOf(onlyPrefix) == 1) {
                newStringBuilder.append(t)
            } else {
                newStringBuilder.append(replaceString)
            }
            replaceString.clear()
        }

        return newStringBuilder.toString()
    }

    companion object {
        const val FALLBACK_LANGUAGE = "eng"

        @JvmStatic
        fun getLanguageList(pathStr: String = ""): Map<String, String> {
            val path = if (pathStr.isEmpty()) {
                CorePaths.RESOURCE_PATH
            } else {
                Paths.get(pathStr)
            }

            val result = HashObjObjMaps.newUpdatableMap<String, String>()

            if (path.isDirectory()) {
                path.listDirectoryEntries("*.ini").forEach {
                    val code = FilenameUtils.removeExtension(path.fileName.toString())
                    val prefs = loadLanguage(it)
                    if (prefs.nodeExists("language.name")) {
                        result[code] = prefs.get("language.name", null) // if the node does not exist, throw NPE
                    }
                }
            }

            return result
        }

        private fun loadLanguage(path: Path): Preferences {
            if (path.exists()) {
                val ini = Ini(path.toFile())
                return IniPreferences(ini)
            } else {
                val languageCode = FilenameUtils.removeExtension(path.fileName.toString())
                throw LanguageNotFoundException("Language \"$languageCode\" not found")
            }
        }
    }

    class LanguageNotFoundException(message: String) : RuntimeException(message)
}
