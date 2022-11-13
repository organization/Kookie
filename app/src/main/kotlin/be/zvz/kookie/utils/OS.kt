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
package be.zvz.kookie.utils

import java.nio.file.FileSystems

enum class OS {
    WINDOWS, LINUX, MAC, SOLARIS, UNKNOWN;

    companion object {
        @JvmStatic
        fun getOS(): OS {
            val os = System.getProperty("os.name").lowercase()
            return when {
                os.contains("win") -> {
                    OS.WINDOWS
                }
                os.contains("nix") || os.contains("nux") || os.contains("aix") -> {
                    OS.LINUX
                }
                os.contains("mac") -> {
                    OS.MAC
                }
                os.contains("sunos") -> {
                    OS.SOLARIS
                }
                else -> OS.UNKNOWN
            }
        }

        @JvmStatic
        val isPosixCompliant = FileSystems.getDefault().supportedFileAttributeViews().contains("posix")
    }
}
