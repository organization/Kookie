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
package be.zvz.kookie.event.player

import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.player.Player

// TODO: Implement this when WritableBook is exist
class PlayerEditBookEvent(player: Player) : PlayerEvent(player), Cancellable {
    override var isCancelled: Boolean = false

    enum class Action(action: Int) {
        REPLACE_PAGE(0),
        ADD_PAGE(1),
        DELETE_PAGE(2),
        SWAP_PAGE(3),
        SIGN_BOOK(4)
    }
}
