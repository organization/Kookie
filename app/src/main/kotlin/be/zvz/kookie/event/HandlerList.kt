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
import com.koloboke.collect.set.hash.HashObjSets

class HandlerList(private val eventClazz: Class<out Event>) {
    private val handlerSlots: MutableMap<EventPriority, MutableSet<RegisteredListener>> =
        HashObjObjMaps.newMutableMap()

    fun register(listener: RegisteredListener) {
        if (!handlerSlots.getOrPut(listener.priority, HashObjSets::newMutableSet).add(listener)) {
            throw IllegalStateException(
                "This listener is already registered to priority ${listener.priority.priority} of event ${eventClazz.simpleName}"
            )
        }
    }

    fun registerAll(vararg listeners: RegisteredListener) {
        listeners.forEach(this::register)
    }

    fun unregister(obj: Plugin) {
        handlerSlots.forEach { (_, slot) ->
            val iter = slot.iterator()
            while (iter.hasNext()) {
                val listener = iter.next()
                if (listener.plugin == obj) {
                    iter.remove()
                }
            }
        }
    }

    fun unregister(obj: Listener) {
        handlerSlots.forEach { (_, slot) ->
            val iter = slot.iterator()
            while (iter.hasNext()) {
                val listener = iter.next()
                if (listener.listener == obj) {
                    iter.remove()
                }
            }
        }
    }

    fun unregister(obj: RegisteredListener) {
        handlerSlots[obj.priority]?.remove(obj)
    }

    fun clear() {
        handlerSlots.clear()
    }

    fun getListenersByPriority(priority: EventPriority): Set<RegisteredListener> = handlerSlots.getValue(priority)
    fun isEmpty(): Boolean = handlerSlots.values.isEmpty()
}
