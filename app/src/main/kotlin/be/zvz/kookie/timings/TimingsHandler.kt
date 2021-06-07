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
package be.zvz.kookie.timings

import java.util.*

class TimingsHandler(val name: String, private val parent: TimingsHandler? = null) {
    var record: TimingsRecord? = null
    var timingsDepth = 0

    fun startTiming() {
        if (enabled) {
            internalStartTiming(Date().time)
        }
    }

    private fun internalStartTiming(now: Long) {
        if (++timingsDepth == 1) {
            record?.startTiming(now) ?: run {
                record = TimingsRecord(this).apply {
                    startTiming(now)
                }
            }
            parent?.internalStartTiming(now)
        }
    }

    fun stopTiming() {
        if (enabled) {
            internalStopTiming(Date().time)
        }
    }

    private fun internalStopTiming(now: Long) {
        if (timingsDepth == 0) {
            return
        }
        if (--timingsDepth != 0) {
            return
        }
        record?.stopTiming(now)
        parent?.internalStopTiming(now)
    }

    fun time(callback: () -> Any): Any {
        startTiming()
        try {
            return callback()
        } finally {
            stopTiming()
        }
    }

    fun destroyCycles() {
        record = null
    }

    companion object {
        var enabled: Boolean = false
        var timingStart: Long = 0

        fun tick(measure: Boolean) {
            TimingsRecord.tick(measure)
        }
    }
}
