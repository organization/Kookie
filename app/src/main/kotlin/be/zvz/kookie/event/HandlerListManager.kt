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
package be.zvz.kookie.event

import be.zvz.kookie.plugin.Plugin
import com.koloboke.collect.map.hash.HashObjObjMaps
import java.lang.reflect.Modifier

internal object HandlerListManager {
    private val handlers: MutableMap<Class<out Event>, HandlerList> = HashObjObjMaps.newMutableMap()

    fun unregisterAll(obj: Plugin) {
        handlers.forEach { (_, it) ->
            it.unregister(obj)
        }
    }

    fun unregisterAll(obj: Listener) {
        handlers.forEach { (_, it) ->
            it.unregister(obj)
        }
    }

    fun unregisterAll() {
        handlers.forEach { (_, it) ->
            it.clear()
        }
    }

    fun getListFor(event: Class<out Event>): HandlerList {
        if (handlers.containsKey(event)) {
            return handlers.getValue(event)
        }
        if (!isValid(event)) {
            throw IllegalArgumentException(
                "Event must be non-abstract or have the ${AllowAbstract::class.java.simpleName} annotation"
            )
        }
        return HandlerList(event).apply {
            handlers[event] = this
        }
    }

    private fun isValid(clazz: Class<out Event>): Boolean {
        val annotation = clazz.getAnnotation(AllowAbstract::class.java)
        return !Modifier.isAbstract(clazz.modifiers) || annotation?.allowed ?: false
    }
}
