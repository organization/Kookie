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
