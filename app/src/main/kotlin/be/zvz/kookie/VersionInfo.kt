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
package be.zvz.kookie

import java.io.IOException
import java.util.Properties

object VersionInfo {
    const val NAME = "Kookie"
    const val IS_DEVELOPMENT_BUILD = true
    const val BUILD_NUMBER = 0
    private val gitPrefs = Properties().apply {
        try {
            load(this::class.java.getResourceAsStream("git.properties"))
        } catch (ignored: IOException) {}
    }
    val GIT_HASH = gitPrefs.getProperty("git.commit.id") ?: ""
    val GIT_IS_DIRTY = gitPrefs.getProperty("git.commit.id") ?: true
    val BASE_VERSION: String = gitPrefs.getProperty("git.build.version") ?: when {
        this::class.java.getPackage() != null && this::class.java.getPackage().implementationVersion != null ->
            this::class.java.getPackage().implementationVersion
        else -> "UNKNOWN"
    }
}
