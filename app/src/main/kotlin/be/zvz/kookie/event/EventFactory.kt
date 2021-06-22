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
import org.slf4j.LoggerFactory
import java.util.concurrent.CancellationException
import java.util.concurrent.ExecutionException
import java.util.concurrent.FutureTask

object EventFactory {
    private val log = LoggerFactory.getLogger(this::class.java)
    fun <T : Event> callEvent(event: T): T {
        if (event.handlers.getRegisteredListeners().isEmpty()) {
            return event
        } else {
            val serverInstance = Server.instance
            if (event.isAsynchronous) {
                // TODO: serverInstance.pluginManager.callEvent(event)
                return event
            } else {
                val task = object : FutureTask<T>(
                    {
                        // TODO: serverInstance.pluginManager::callEvent
                    },
                    event
                ) {
                    // TODO: callEvent
                }
                return try {
                    task.get()
                } catch (e: InterruptedException) {
                    log.warn(event::class.java.simpleName, e)
                    event
                } catch (e: CancellationException) {
                    log.warn(event::class.java.simpleName, e)
                    event
                } catch (e: ExecutionException) {
                    throw EventExecutionException(e)
                }
            }
        }
    }

    class EventExecutionException(cause: Throwable? = null) : RuntimeException(cause)
}
