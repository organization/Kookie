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

import be.zvz.kookie.plugin.Plugin
import com.koloboke.collect.map.hash.HashObjObjMaps

class HandlerList @JvmOverloads constructor(
    val clazz: Class<out Event>,
    val parentHandlerList: HandlerList? = null
) {
    var handlerSlots: MutableMap<EventPriority, MutableList<RegisteredListener>> =
        HashObjObjMaps.newMutableMap()
        private set

    fun register(listener: RegisteredListener) {
        if (handlerSlots.getOrPut(listener.priority) {
            mutableListOf()
        }.contains(listener)
        ) {
            throw IllegalStateException(
                "This listener is already registered to priority ${listener.priority.priority} of event ${clazz.simpleName}"
            )
        }
        handlerSlots.getOrPut(listener.priority) {
            mutableListOf()
        }.add(listener)
    }

    fun registerAll(vararg listeners: RegisteredListener) {
        listeners.forEach {
            register(it)
        }
    }

    fun unregister(obj: Plugin) {
        val iter = handlerSlots.iterator()
        while (iter.hasNext()) {
            val (priority, list) = iter.next()
            list.forEach {
                if (it.plugin == obj) {
                    iter.remove()
                }
            }
        }
    }

    fun unregister(obj: Listener) {
        val iter = handlerSlots.iterator()
        while (iter.hasNext()) {
            val (priority, list) = iter.next()
            list.forEach {
                iter.remove()
            }
        }
    }

    fun unregister(obj: RegisteredListener) {
        if (handlerSlots[obj.priority]?.contains(obj) == true) {
            handlerSlots[obj.priority]?.remove(obj)
        }
    }

    fun clear() {
        handlerSlots = HashObjObjMaps.newMutableMap()
    }

    fun getListenersByPriority(priority: EventPriority): MutableList<RegisteredListener>? = handlerSlots[priority]

    fun getParent(): HandlerList? = parentHandlerList
}
