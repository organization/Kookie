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
package be.zvz.kookie.plugin

import be.zvz.kookie.event.Event
import be.zvz.kookie.event.EventPriority
import be.zvz.kookie.event.HandlerListManager
import be.zvz.kookie.event.Listener
import be.zvz.kookie.event.RegisteredListener
import be.zvz.kookie.timings.TimingsHandler

class PluginManager {
    @JvmOverloads
    fun registerEvent(
        eventClass: Class<out Event>,
        listener: Listener,
        handler: (Listener, Event) -> Unit,
        priority: EventPriority,
        plugin: Plugin,
        handleCancelled: Boolean = false
    ) {
        if (Event::class.java.isAssignableFrom(eventClass)) {
            throw PluginException("${eventClass.simpleName} is not an Event") // fix - unchecked cast
        }
        val timings = TimingsHandler(
            "Plugin: ${plugin.description.fullName} Event: ${handler::class.java.simpleName}(${eventClass.simpleName})"
        )
        HandlerListManager.getListFor(eventClass).register(
            RegisteredListener(
                {
                    handler(listener, it)
                },
                priority,
                plugin,
                handleCancelled,
                timings
            )
        )
    }

    @JvmOverloads
    fun registerEvent(
        eventClass: Class<out Event>,
        handler: (Event) -> Unit,
        priority: EventPriority,
        plugin: Plugin,
        handleCancelled: Boolean = false
    ) {
        if (Event::class.java.isAssignableFrom(eventClass)) {
            throw PluginException("${eventClass.simpleName} is not an Event") // fix - unchecked cast
        }
        val timings = TimingsHandler(
            "Plugin: ${plugin.description.fullName} Event: ${handler::class.java.simpleName}(${eventClass.simpleName})"
        )
        HandlerListManager.getListFor(eventClass).register(
            RegisteredListener(
                handler,
                priority,
                plugin,
                handleCancelled,
                timings
            )
        )
    }
}
