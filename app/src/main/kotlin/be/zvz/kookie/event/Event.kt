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

abstract class Event {

    val eventName: String? = null
        get() {
            return field ?: this::class.java.simpleName
        }

    fun call() {
        if (eventCallDepth >= MAX_EVENT_CAL_DEPTH) {
            throw RuntimeException("Recursive event call detected (reached max depth of $MAX_EVENT_CAL_DEPTH calls)")
        }
        ++eventCallDepth

        try {
            EventPriority.ALL.forEach {
                HandlerListManager.handlers.forEach {
                }
            }
        } finally {
            --eventCallDepth
        }
    }

    companion object {
        private const val MAX_EVENT_CAL_DEPTH = 50
        private var eventCallDepth = 1
    }
}
