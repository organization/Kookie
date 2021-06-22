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
package be.zvz.kookie.event.server

import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.event.HandlerList
import be.zvz.kookie.network.NetworkInterface

class NetworkInterfaceRegisterEvent(networkInterface: NetworkInterface) : NetworkInterfaceEvent(networkInterface), Cancellable {
    override var isCancelled: Boolean = false

    override val handlers: HandlerList
        get() = handlerList

    companion object {
        private val handlerList = HandlerList(NetworkInterfaceRegisterEvent::class.java)
    }
}
