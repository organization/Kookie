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

import com.koloboke.collect.map.hash.HashObjObjMaps
import org.apache.commons.io.FilenameUtils
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.Properties
import java.util.jar.JarFile

class Language @JvmOverloads constructor(langStr: String, path: String = "locale", fallback: String = FALLBACK_LANGUAGE) {
    private val langName: String = langStr.lowercase()
    private val languagePrefs: Properties = loadLanguage(path, langStr)
    private val fallbackLang: Properties = loadLanguage(path, fallback)

    val name: String get() = languagePrefs.getProperty("language.name")!!
    val lang: String get() = langName

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
        str: KnownTranslationKeys,
        onlyPrefix: String? = null
    ): String = translateString(str.key, onlyPrefix)

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

    @JvmOverloads
    fun translateString(
        str: KnownTranslationKeys,
        params: Map<String, String>,
        onlyPrefix: String? = null
    ): String = translateString(str.key, params, onlyPrefix)

    @JvmOverloads
    fun translateString(
        str: KnownTranslationKeys,
        params: List<String>,
        onlyPrefix: String? = null
    ): String = translateString(str.key, params, onlyPrefix)

    fun translate(c: TranslationContainer): String = (internalGet(c.text) ?: parseTranslation(c.text)).let {
        var replacedStr = it
        c.params.forEachIndexed { index, p ->
            replacedStr = replacedStr.replace("{%$index}", parseTranslation(p))
        }
        replacedStr
    }

    private fun internalGet(id: String): String? = languagePrefs.getProperty(id, fallbackLang.getProperty(id))
    fun get(id: String): String = internalGet(id) ?: id
    fun get(id: KnownTranslationKeys): String = get(id.key)

    @JvmOverloads
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
                    it in arrayOf('.', '-')
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
        @JvmOverloads
        fun getLanguageList(pathStr: String = "locale"): Map<String, String> {
            val result = HashObjObjMaps.newUpdatableMap<String, String>()

            val jarFile = File(this::class.java.protectionDomain.codeSource.location.path)

            mutableListOf<String>().apply {
                if (jarFile.isFile) { // Run with JAR file
                    val jar = JarFile(jarFile)
                    val entries = jar.entries() // gives ALL entries in jar
                    while (entries.hasMoreElements()) {
                        val name: String = entries.nextElement().name
                        if (name.startsWith("$pathStr/") && name.endsWith(".ini")) { // filter according to the path
                            add(FilenameUtils.getBaseName(name))
                        }
                    }
                    jar.close()
                } else { // Run with IDE
                    this::class.java.getResource("/$pathStr")?.let {
                        val apps = File(it.toURI())
                        apps.listFiles()?.forEach { file ->
                            if (file.path.endsWith(".ini")) {
                                add(FilenameUtils.getBaseName(file.path))
                            }
                        }
                    }
                }
            }.forEach { lang ->
                val prefs = loadLanguage(pathStr, lang)
                if (prefs.containsKey("language.name")) {
                    result[lang] = prefs.getProperty("language.name")
                }
            }

            return result.ifEmpty {
                throw LanguageNotFoundException("Language directory $pathStr does not exist or is not a directory")
            }
        }

        private fun loadLanguage(localeDir: String, name: String): Properties {
            this::class.java.getResourceAsStream("/$localeDir/$name.ini")?.use {
                return Properties().apply {
                    InputStreamReader(it, Charsets.UTF_8).use { isr ->
                        BufferedReader(isr).use { br ->
                            load(br)
                        }
                    }
                }
            } ?: throw LanguageNotFoundException("Language \"$name\" not found")
        }
    }

    class LanguageNotFoundException(message: String) : RuntimeException(message)
}
