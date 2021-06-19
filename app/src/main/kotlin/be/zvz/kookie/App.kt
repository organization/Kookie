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

import be.zvz.kookie.constant.CorePaths
import be.zvz.kookie.constant.FilePermission
import be.zvz.kookie.wizard.SetupWizard
import org.slf4j.LoggerFactory
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.setPosixFilePermissions
import kotlin.system.exitProcess

class App {
    private val logger = LoggerFactory.getLogger(App::class.java)

    fun start(): Int {
        val cwd = CorePaths.PATH
        val dataPath = cwd.resolve("data").apply {
            if (!exists()) {
                createDirectories()
                setPosixFilePermissions(FilePermission.perm777)
            }
        }
        val pluginPath = cwd.resolve("plugins").apply {
            if (!exists()) {
                createDirectories()
                setPosixFilePermissions(FilePermission.perm777)
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

        var exitCode = 0
        run {
            if (!cwd.resolve(SERVER_PROPERTIES_NAME).exists()) {
                val wizard = SetupWizard(cwd)

                if (!wizard.run()) {
                    exitCode = -1
                    return@run
                }
            }

            try {
                Server(cwd, dataPath, pluginPath)
            } catch (e: Throwable) {
                logger.error("Critical Error", e)
            }
        }

        logger.info("Stopping Kookie")

        lockFilePath.deleteIfExists()

        return exitCode
    }

    companion object {
        const val SERVER_PROPERTIES_NAME = "server.properties"
    }
}

fun main() {
    exitProcess(App().start())
}
