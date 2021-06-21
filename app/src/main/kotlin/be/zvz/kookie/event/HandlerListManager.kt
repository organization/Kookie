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
import java.lang.reflect.Modifier

object HandlerListManager {
    private val handlers: MutableMap<Class<*>, HandlerList> = HashObjObjMaps.newMutableMap()

    @JvmStatic
    @JvmOverloads
    fun unregisterAll(obj: Any? = null) {
        when (obj) {
            is Plugin -> {
                handlers.forEach { (_, it) ->
                    it.unregister(obj)
                }
            }
            else -> {
                handlers.forEach { (_, it) ->
                    it.clear()
                }
            }
        }
    }

    fun getListFor(event: Class<*>): HandlerList {
        if (handlers.containsKey(event)) {
            return handlers.getValue(event)
        }
        if (!isValid(event)) {
            throw IllegalArgumentException(
                "Event must be non-abstract or have the ${AllowAbstract::class.java.simpleName} annotation"
            )
        }
        return HandlerList(event, resolveNearestHandleableParent(event)?.let(::getListFor)).apply {
            handlers[event] = this
        }
    }

    private fun isValid(clazz: Class<*>): Boolean {
        val annotation = clazz.getAnnotation(AllowAbstract::class.java)
        return !Modifier.isAbstract(clazz.modifiers) || annotation?.allowed ?: false
    }

    private fun resolveNearestHandleableParent(clazz: Class<*>): Class<*>? {
        var parent = clazz.superclass
        while (parent !== null) {
            if (isValid(parent)) {
                return parent
            }
            parent = parent.superclass
        }
        return null
    }
}
