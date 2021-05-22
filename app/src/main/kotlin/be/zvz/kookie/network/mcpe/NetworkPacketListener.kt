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
package be.zvz.kookie.network.mcpe

import com.nukkitx.network.raknet.EncapsulatedPacket
import com.nukkitx.network.raknet.RakNetSessionListener
import com.nukkitx.network.raknet.RakNetState
import com.nukkitx.network.util.DisconnectReason
import io.netty.buffer.ByteBuf

class NetworkPacketListener(private val session: NetworkSession) : RakNetSessionListener {
    override fun onSessionChangeState(state: RakNetState) {
        TODO("Not yet implemented")
    }

    override fun onDisconnect(reason: DisconnectReason) {
        TODO("Not yet implemented")
    }

    override fun onEncapsulated(packet: EncapsulatedPacket) {
        TODO("Not yet implemented")
    }

    override fun onDirect(buf: ByteBuf) {
        session.handleBuffer(buf)
    }
}
