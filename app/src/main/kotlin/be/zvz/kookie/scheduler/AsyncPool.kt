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
package be.zvz.kookie.scheduler

import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class AsyncPool(
    maxPoolSize: Int,
) {
    private val scheduledThreadPool = Executors.newScheduledThreadPool(maxPoolSize)
    fun <T> submit(task: AsyncTask<T>): Future<T>? {
        if (task.isSubmitted()) {
            throw IllegalArgumentException("Cannot submit the same AsyncTask instance more than once")
        }
        task.setSubmitted()
        return scheduledThreadPool.submit(task)
    }

    fun <T> schedule(id: Long, task: AsyncTask<T>, delay: Long, timeunit: TimeUnit): ScheduledFuture<T>? {
        if (task.isSubmitted()) {
            throw IllegalArgumentException("Cannot schedule the same AsyncTask instance more than once")
        }
        task.setSubmitted()
        return scheduledThreadPool.schedule(task, delay, timeunit)
    }
}
