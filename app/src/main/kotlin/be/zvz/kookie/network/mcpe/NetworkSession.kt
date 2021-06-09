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
import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.ClientboundPacket
import be.zvz.kookie.network.mcpe.protocol.DataPacket
import be.zvz.kookie.network.mcpe.protocol.Packet
import be.zvz.kookie.network.mcpe.protocol.UpdateAttributesPacket
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.entity.NetworkAttribute
import be.zvz.kookie.player.Player
import be.zvz.kookie.player.PlayerInfo
import be.zvz.kookie.timings.Timings
import com.nukkitx.network.raknet.RakNetSession
import com.nukkitx.network.util.DisconnectReason
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Date

class NetworkSession(
    private val server: Server,
    private val sessionManager: NetworkSessionManager,
    private val session: RakNetSession,
) {

    val logger: Logger = LoggerFactory.getLogger(NetworkSession::class.java)
    private var ping: Int? = null
    private var player: Player? = null
    private var info: PlayerInfo? = null
    private var connected = false
    private var connectedTime = Date()
    private val sendBuffer = mutableListOf<Packet>()

    private var handler: PacketHandlerInterface? = null

    init {
        // TODO: setHandler(LoginPacketHandler) here
    }

    fun setHandler(handler: PacketHandlerInterface? = null) {
        this.handler = handler
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

    fun sendDataPacket(packet: DataPacket, immediate: Boolean = false) {
        // TODO: call DataPacketSendEvent on here
        val timings = Timings.getPacketSendTimings(packet)
        timings.startTiming()
        try {
            if (packet !is ClientboundPacket) {
                throw InvalidPacketException("Cannot send non-clientbound packet to ${getDisplayName()}")
            }
            if (info == null) {
                if (!packet.canBeSentBeforeLogin()) {
                    throw InvalidPacketException("Cannot send ${packet.getName()} to ${getDisplayName()} before login")
                }
            }

            sendBuffer.add(packet)
            sessionManager.scheduleUpdate(this)
        } finally {
            timings.stopTiming()
        }
    }

    fun handleDataPacket(packet: DataPacket, buffer: ByteBuf) {
        val timings = Timings.getPacketDecodeTimings(packet)
        timings.startTiming()
        try {
            val serializer = PacketSerializer(buffer.toString())
            packet.decode(serializer)
            if (!serializer.feof()) {
                val remains = serializer.buffer.substring(serializer.offset.get())
                logger.debug("Still ${remains.length} bytes unread in ${packet.getName()}")
            }
            handler?.let {
                if (!packet.handle(it)) {
                    logger.debug("Unhandled ${packet.getName()}")
                }
            }
        } finally {
            timings.stopTiming()
        }
    }

    fun handleBuffer(buffer: ByteBuf) {
    }

    fun syncAttributes(entity: Living, attributes: Map<String, Attribute>) {
        if (attributes.isNotEmpty()) {
            val networkAttributes: MutableList<NetworkAttribute> = mutableListOf()
            attributes.forEach { (id, attribute) ->
                networkAttributes.add(
                    NetworkAttribute(
                        id,
                        attribute.minValue,
                        attribute.maxValue,
                        attribute.currentValue,
                        attribute.defaultValue
                    )
                )
            }
            val pk = UpdateAttributesPacket()
            pk.entityRuntimeId = 0L // TODO: Entity runtime id
            pk.entries = networkAttributes
            sendDataPacket(pk)
        }
    }

    private fun flushSendBuffer(immediate: Boolean = false) {
        try {
            if (sendBuffer.size > 0) {
                if (immediate) {
                    sendBuffer.forEach {
                        val serializer = PacketSerializer()
                        it.encode(serializer)
                        // TODO: packet encryption here
                        session.send(Unpooled.copiedBuffer(serializer.buffer.toString().toByteArray()))
                    }
                    return
                }
                // TODO: non-immediate buffer, should compressed with zlib
            }
        } finally {
            sendBuffer.clear()
        }
    }

    @JvmOverloads
    fun disconnect(reason: String, notify: Boolean = true) {
        session.disconnect(DisconnectReason.DISCONNECTED)
    }

    fun getDisplayName(): String {
        return info?.getUsername() ?: session.address.toString()
    }

    class InvalidPacketException(message: String) : RuntimeException(message)
}
