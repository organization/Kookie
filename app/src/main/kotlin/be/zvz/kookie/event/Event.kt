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
package be.zvz.kookie.event

import be.zvz.kookie.Server
import be.zvz.kookie.scheduler.AsyncTask

abstract class Event @JvmOverloads constructor(
    val isAsynchronous: Boolean = false
) {

    val eventName: String? = null
        get() {
            return field ?: this::class.java.simpleName
        }

    fun call() {
        val fire = {
            HandlerListManager.getListFor(this::class.java).getRegisteredListeners().forEach {
                if (!it.plugin.enabled) {
                    return@forEach
                }

                it.callEvent(this)
            }
        }

        if (isAsynchronous) {
            Server.instance.asyncPool.submit(AsyncTask(fire, null))
        } else {
            synchronized(Server.instance.pluginManager) {
                fire()
            }
        }
    }
}
