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
package be.zvz.kookie.player

import be.zvz.kookie.nbt.tag.CompoundTag

class OfflinePlayer(override val name: String, val namedTag: CompoundTag?) : IPlayer {

    override var firstPlayed: Long = namedTag?.getLong("firstPlayed") ?: 0
    override var lastPlayed: Long = namedTag?.getLong("lastPlayed") ?: 0

    override fun hasPlayedBefore(): Boolean {
        return namedTag != null && firstPlayed != -1L
    }
}
