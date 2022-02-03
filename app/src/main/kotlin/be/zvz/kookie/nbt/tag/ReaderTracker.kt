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
package be.zvz.kookie.nbt.tag

import be.zvz.kookie.nbt.NbtDataException

class ReaderTracker(private val maxDepth: Int) {
    private var currentDepth = 0
    fun protectDepth(execute: () -> Unit) {
        if (maxDepth > 0 && ++currentDepth > maxDepth) {
            throw NbtDataException("Nesting level too deep: reached max depth of this.maxDepth tags")
        }
        try {
            execute()
        } finally {
            --currentDepth
        }
    }
}
