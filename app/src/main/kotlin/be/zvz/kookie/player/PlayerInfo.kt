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
package be.zvz.kookie.player

import be.zvz.kookie.entity.Skin
import java.util.*

class PlayerInfo(
    private val username: String,
    private val uuid: UUID,
    private val skin: Skin,
    private val locale: String,
    private val extraData: Nothing? = null
) {

    fun getUsername(): String {
        return username
    }

    fun getUUID(): UUID {
        return uuid
    }

    fun getSkin(): Skin {
        return skin // TODO: change this to its own class when it is possible
    }

    fun getLocale(): String {
        return locale
    }

    fun getExtraData(): Nothing? {
        return extraData // TODO: change this to extra data class
    }
}
