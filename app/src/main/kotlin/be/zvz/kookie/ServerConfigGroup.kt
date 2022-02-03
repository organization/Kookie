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
package be.zvz.kookie

import be.zvz.kookie.utils.Config
import be.zvz.kookie.utils.config.ConfigBrowser
import com.koloboke.collect.map.hash.HashObjObjMaps

class ServerConfigGroup(
    private val kookieYml: Config,
    private val serverProperties: Config
) {

    private val propertyCache: MutableMap<String, ConfigBrowser> = HashObjObjMaps.newMutableMap()

    @JvmOverloads
    fun getProperty(
        variable: String,
        defaultValue: ConfigBrowser = ConfigBrowser.NULL_BROWSER
    ): ConfigBrowser = propertyCache.getOrPut(variable) { kookieYml.get(variable, defaultValue) }

    @JvmOverloads
    fun getConfigString(variable: String, defaultValue: String = ""): String =
        if (serverProperties.exists(variable)) {
            serverProperties.get(variable).safeText()
        } else {
            defaultValue
        }

    fun setConfigString(variable: String, value: String) = serverProperties.set(variable, value)

    @JvmOverloads
    fun getConfigLong(variable: String, defaultValue: Long = 0): Long =
        serverProperties.get(variable).asLong(defaultValue)

    fun setConfigLong(variable: String, value: Long) =
        serverProperties.set(variable, value)

    @JvmOverloads
    fun getConfigBoolean(variable: String, defaultValue: Boolean = false): Boolean =
        serverProperties.get(variable).asBoolean(defaultValue)

    fun setConfigBoolean(variable: String, value: Boolean) =
        serverProperties.set(variable, value)

    fun save() {
        if (serverProperties.hasChanged()) {
            serverProperties.save()
        }
        if (kookieYml.hasChanged()) {
            kookieYml.save()
        }
    }
}
