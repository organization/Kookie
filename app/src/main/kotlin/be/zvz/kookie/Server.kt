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

import be.zvz.kookie.console.KookieConsole
import be.zvz.kookie.constant.FilePermission
import be.zvz.kookie.snooze.SleeperHandler
import org.slf4j.LoggerFactory
import java.nio.file.Path
import java.util.*
import kotlin.concurrent.thread
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.setPosixFilePermissions

class Server(cwd: Path, dataPath: Path, pluginPath: Path) {
    private var tickSleeper = SleeperHandler()
    /**
     * Counts the ticks since the server start
     *
     */
    private var tickCounter: Int = 0
    private var nextTick: Float = 0F
    private val tickAverage: Array<Float> = arrayOf(20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F, 20F)
    private val useAverage: Array<Float> = arrayOf(0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F)
    private var currentTPS: Float = 20F
    private var currentUse: Float = 0F
    private val startTime: Date

    private var doTitleTick = true
    private val logger = LoggerFactory.getLogger(Server::class.java)
    private val console = KookieConsole()

    init {
        instance = this
        startTime = Date()

        val worldsPath = dataPath.resolve("worlds")
        if (!worldsPath.exists()) {
            worldsPath.createDirectories()
            worldsPath.setPosixFilePermissions(FilePermission.perm777)
        }
        val playersPath = dataPath.resolve("players")
        if (!playersPath.exists()) {
            playersPath.createDirectories()
            playersPath.setPosixFilePermissions(FilePermission.perm777)
        }
        if (pluginPath.exists()) {
            pluginPath.createDirectories()
            pluginPath.setPosixFilePermissions(FilePermission.perm777)
        }

        thread(isDaemon = true, name = "Kookie-console") {
            console.start()
        }
    }

    companion object {
        @JvmStatic
        lateinit var instance: Server
    }
}
