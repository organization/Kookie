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

class SynchronousScheduler(scheduler: TaskScheduler) : AbstractScheduler(scheduler) {
    override fun schedule(runnable: Runnable): TaskHandler {
        return scheduler.scheduleTask(object : Task() {
            override fun onRun() {
                runnable.run()
            }
        })
    }

    override fun schedule(runnable: Runnable, delay: Int): TaskHandler {
        return scheduler.scheduleDelayedTask(
            object : Task() {
                override fun onRun() {
                    runnable.run()
                }
            },
            delay
        )
    }

    override fun schedule(runnable: Runnable, delay: Int, interval: Int): TaskHandler {
        return scheduler.scheduleTask(
            object : Task() {
                override fun onRun() {
                    runnable.run()
                }
            },
            delay, interval
        )
    }
}
