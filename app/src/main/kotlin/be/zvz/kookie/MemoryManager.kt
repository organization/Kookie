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

import org.slf4j.LoggerFactory

class MemoryManager(private val server: Server) {
    private val logger = LoggerFactory.getLogger(MemoryManager::class.java)
    private var lowMemory = false
    var globalMemoryLimit = 0
        private set
    private var lowMemDisableChunkCache = false

    init {
        init(server.configGroup)
    }

    private fun init(config: ServerConfigGroup) {}

    fun isLowMemory(): Boolean = lowMemory

    fun canUseChunkCache(): Boolean = !lowMemory || !lowMemDisableChunkCache

    fun getViewDistance(distance: Int): Int {
        return 0
    }

    fun trigger(memory: Int, limit: Int, global: Boolean = false, triggerCount: Int = 0) {}

    fun check() {}

    fun triggerGarbageCollector(): Int {
        return 0
    }

    fun dumpServerMemory(outputFolder: String, maxNesting: Int, maxStringSize: Int) {}

    companion object {
        @JvmStatic
        fun dumpMemory(
            startingObject: Any,
            outputFolder: String,
            maxNesting: Int,
            maxStringSize: Int
        ) {}

        @JvmStatic
        private fun continueDump(
            from: Any,
            objects: List<Any>,
            refCounts: List<Int>,
            recursion: Int,
            maxNesting: Int,
            maxStringSize: Int
        ) {
        }
    }
}
