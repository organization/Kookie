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
package be.zvz.kookie.scheduler

import be.zvz.kookie.timings.Timings
import be.zvz.kookie.timings.TimingsHandler

open class TaskHandler @JvmOverloads constructor(
    task: Task,
    delay: Int = -1,
    period: Int = -1,
    ownerName: String? = null
) {
    var task = task
        protected set
    var delay = delay
        protected set
    var period = period
        protected set
    var nextRun = 0L
    var isCancelled = false
        protected set
    val taskName: String = task.name
    val ownerName = ownerName ?: "Unknown"
    private val timings: TimingsHandler by lazy {
        Timings.getScheduledTaskTimings(this, period)
    }

    open fun cancel() {
        try {
            task.onCancel()
        } finally {
            remove()
        }
    }

    open fun remove() {
        isCancelled = true
        task.handler = null
    }

    open fun run() {
        timings.startTiming()
        try {
            task.onRun()
        } catch (_: CancelTaskException) {
            cancel()
        } finally {
            timings.stopTiming()
        }
    }

    open fun isDelayed(): Boolean = delay > 0
}
