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

import be.zvz.kookie.Server
import be.zvz.kookie.entity.Attribute
import be.zvz.kookie.entity.Living
import be.zvz.kookie.network.mcpe.protocol.ClientboundPacket
import be.zvz.kookie.network.mcpe.protocol.DataPacket
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer
import be.zvz.kookie.player.Player
import be.zvz.kookie.player.PlayerInfo
import com.nukkitx.network.raknet.RakNetSession
import com.nukkitx.network.util.DisconnectReason
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.slf4j.LoggerFactory
import java.util.*

class NetworkSession(private val server: Server, private val session: RakNetSession) {

    val logger = LoggerFactory.getLogger(NetworkSession::class.java)
    private var ping: Int? = null
    private var player: Player? = null
    private var info: PlayerInfo? = null
    private var connected = false
    private var connectedTime = Date()
    private val sendBuffer = mutableListOf<ByteBuf>()

    init {
    }

    fun tick(): Boolean {
        info?.let {
            if (connectedTime.time <= Date().time + 10) {
                disconnect("Login Timeout")
                return false
            }
            return true
        }

        player?.let {
            it.doChunkRequest()
            val dirtyAttributes = it.attributeMap.needSend()
            syncAttributes(it, dirtyAttributes)
            dirtyAttributes.forEach { (_, attribute) ->
                attribute.markSynchronized()
            }
        }

        flushSendBuffer()
        return true
    }

    fun sendDataPacket(packet: DataPacket) {
        // TODO: call DataPacketSendEvent on here
        if (packet !is ClientboundPacket) {
            throw InvalidPacketException("Cannot send non-clientbound packet to player")
        }
        if (info == null) {
            if (!packet.canBeSentBeforeLogin()) {
                throw InvalidPacketException("Cannot send ${packet.getName()} before login")
            }
        }

        val serializer = PacketSerializer()
        packet.encode(serializer)
        val buffer = serializer.buffer
        putBuffer(Unpooled.copiedBuffer(buffer.toString().toByteArray()))
    }

    fun putBuffer(buffer: ByteBuf) {
        sendBuffer.add(buffer)
    }

    fun syncAttributes(entity: Living, attributes: Map<String, Attribute>) {
        if (attributes.isNotEmpty()) {
            TODO("sendDataPacket")
        }
    }

    private fun flushSendBuffer(immediate: Boolean = false) {
        if (sendBuffer.size > 0) {
            sendBuffer.forEach {
                session.send(it)
            }
            sendBuffer.clear()
        }
    }

    @JvmOverloads
    fun disconnect(reason: String, notify: Boolean = true) {
        session.disconnect(DisconnectReason.DISCONNECTED)
    }

    class InvalidPacketException(message: String) : RuntimeException(message)
}
