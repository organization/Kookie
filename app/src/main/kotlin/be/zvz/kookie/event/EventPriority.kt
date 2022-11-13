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

enum class EventPriority(val priority: Int) {
    /**
     * Event call is of very low importance and should be ran first, to allow
     * other plugins to further customise the outcome
     */
    LOWEST(5),

    /**
     * Event call is of low importance
     */
    LOW(4),

    /**
     * Event call is neither important or unimportant, and may be ran normally.
     * This is the default priority.
     */
    NORMAL(3),

    /**
     * Event call is of high importance
     */
    HIGH(2),

    /**
     * Event call is critical and must have the final say in what happens
     * to the event
     */
    HIGHEST(1),

    /**
     * Event is listened to purely for monitoring the outcome of an event.
     *
     * No modifications to the event should be made under this priority
     */
    MONITOR(0);

    companion object {
        val ALL = values()
        @JvmStatic fun from(value: Int) = ALL.first { it.priority == value }
    }
}
