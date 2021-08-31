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
import be.zvz.kookie.player.Player
import be.zvz.kookie.player.PlayerInfo
import be.zvz.kookie.timings.Timings
import com.nukkitx.protocol.bedrock.BedrockPacket
import com.nukkitx.protocol.bedrock.BedrockSession
import com.nukkitx.protocol.bedrock.data.AttributeData
import com.nukkitx.protocol.bedrock.packet.UpdateAttributesPacket
import io.netty.buffer.ByteBuf
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Date

class NetworkSession(
    private val server: Server,
    private val sessionManager: NetworkSessionManager,
    private val session: BedrockSession,
) {

    val logger: Logger = LoggerFactory.getLogger(NetworkSession::class.java)
    private var ping: Int? = null
    private var player: Player? = null
    private var info: PlayerInfo? = null
    private var connected = false
    private var connectedTime = Date()
    private val sendBuffer = mutableListOf<BedrockPacket>()

    private var handler: PacketHandlerInterface? = null

    init {
        // TODO: setHandler(LoginPacketHandler) here
    }

    @JvmOverloads
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
            dirtyAttributes.values.forEach { attribute ->
                attribute.markSynchronized()
            }
        }
        return true
    }

    @JvmOverloads
    fun sendDataPacket(packet: BedrockPacket, immediate: Boolean = false) {
        // TODO: call DataPacketSendEvent on here
        val timings = Timings.getPacketSendTimings(packet)
        timings.startTiming()
        try {
            if (info == null /*&& !packet.canBeSentBeforeLogin()*/) {
                throw InvalidPacketException("Cannot send ${packet::class.java.simpleName} to ${getDisplayName()} before login")
            }

            sendBuffer.add(packet)
            sessionManager.scheduleUpdate(this)
        } finally {
            timings.stopTiming()
        }
    }

    fun handleBuffer(buffer: ByteBuf) {
    }

    fun syncAttributes(entity: Living, attributes: Map<Attribute.Identifier, Attribute>) {
        if (attributes.isNotEmpty()) {
            val networkAttributes: MutableList<AttributeData> = mutableListOf()
            attributes.forEach { (id, attribute) ->
                networkAttributes.add(
                    AttributeData(
                        id.name,
                        attribute.minValue,
                        attribute.maxValue,
                        attribute.currentValue,
                        attribute.defaultValue
                    )
                )
            }
            val pk = UpdateAttributesPacket()
            pk.runtimeEntityId = 0L // TODO: Entity runtime id
            pk.attributes = networkAttributes
            sendDataPacket(pk)
        }
    }

    @JvmOverloads
    fun disconnect(reason: String, notify: Boolean = true) {
        session.disconnect()
    }

    fun getDisplayName(): String {
        return info?.getUsername() ?: session.address.toString()
    }

    class InvalidPacketException(message: String) : RuntimeException(message)
}
