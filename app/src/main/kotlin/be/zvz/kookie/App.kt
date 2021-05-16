/**
 * Kookie
 *
 * Copyright (C) 2021 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie

import org.slf4j.LoggerFactory
import java.nio.file.Paths
import java.nio.file.attribute.PosixFilePermission
import kotlin.io.path.*
import kotlin.system.exitProcess

class App {
    private val logger = LoggerFactory.getLogger(App::class.java)

    fun start() {
        val cwd = Paths.get("").toAbsolutePath()
        val permission777 = setOf(
            PosixFilePermission.GROUP_EXECUTE,
            PosixFilePermission.GROUP_READ,
            PosixFilePermission.GROUP_WRITE,
            PosixFilePermission.OTHERS_EXECUTE,
            PosixFilePermission.OTHERS_READ,
            PosixFilePermission.OTHERS_WRITE,
            PosixFilePermission.OWNER_EXECUTE,
            PosixFilePermission.OWNER_WRITE,
            PosixFilePermission.OWNER_READ
        )
        val dataPath = cwd.resolve("data").apply {
            if (!exists()) {
                createDirectories()
                setPosixFilePermissions(permission777)
            }
        }
        val pluginPath = cwd.resolve("plugins").apply {
            if (!exists()) {
                createDirectories()
                setPosixFilePermissions(permission777)
            }
        }
        val lockFilePath = cwd.resolve("server.lock").apply {
            if (!exists()) {
                createFile()
            } else {

                logger.error("Another instance is already using this folder ($this)")
                logger.error("Please stop the other server first before running a new one.")
                exitProcess(1)
            }
        }

        if (!cwd.resolve(SERVER_PROPERTIES_NAME).exists()) {
        }
        Server(cwd, dataPath, pluginPath)
    }

    companion object {
        const val SERVER_PROPERTIES_NAME = "server.properties"
    }
}

fun main() {
}
