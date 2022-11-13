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
package be.zvz.kookie.network

import io.netty.buffer.ByteBuf
import java.net.InetSocketAddress

interface AdvancedNetworkInterface : NetworkInterface {

    /**
     * Prevents packets received from the IP address getting processed for the given timeout.
     */
    fun blockAddress(address: InetSocketAddress, timeout: Long)

    /**
     * Unblocks a previously-blocked address.
     */
    fun unblockAddress(address: InetSocketAddress)

    fun setNetwork(network: Network)

    /**
     * Sends a raw payload to the network interface, bypassing any sessions.
     */
    fun sendRawPacket(address: InetSocketAddress, payload: ByteBuf)

    /**
     * Adds a regex filter for raw packets to this network interface. This filter should be used to check validity of
     * raw packets before relaying them to the main thread.
     */
    fun addRawPacketFilter(regex: Regex)
}
