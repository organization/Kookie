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
package be.zvz.kookie.world.light

import com.koloboke.collect.map.hash.HashLongObjMaps
import java.util.LinkedList
import java.util.Queue

class LightPropagationContext {
    val spreadQueue: Queue<Triple<Int, Int, Int>> = LinkedList()
    val spreadVisited: MutableMap<Long, Boolean> = HashLongObjMaps.newMutableMap()

    val removalQueue: Queue<LightNode> = LinkedList()
    val removalVisited: MutableMap<Long, Boolean> = HashLongObjMaps.newMutableMap()
}
