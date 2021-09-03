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
import be.zvz.kookie.network.mcpe.handler.LoginPacketHandler
import be.zvz.kookie.player.Player
import be.zvz.kookie.player.PlayerInfo
import com.nukkitx.protocol.bedrock.BedrockPacket
import com.nukkitx.protocol.bedrock.BedrockServerSession
import com.nukkitx.protocol.bedrock.data.AttributeData
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler
import com.nukkitx.protocol.bedrock.packet.UpdateAttributesPacket
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Date

class NetworkSession(
    private val server: Server,
    private val sessionManager: NetworkSessionManager,
    private val session: BedrockServerSession,
) {

    val logger: Logger = LoggerFactory.getLogger(NetworkSession::class.java)
    private var ping: Int? = null
    private var player: Player? = null
    private var info: PlayerInfo? = null
    private var connected = false
    private var connectedTime = Date()

    private var handler: BedrockPacketHandler? = null

    init {
        setHandler(
            LoginPacketHandler(this, { info ->
                // TODO
            }, {
                // TODO
            })
        )
    }

    @JvmOverloads
    fun setHandler(handler: BedrockPacketHandler? = null) {
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
        if (immediate) session.sendPacketImmediately(packet) else session.sendPacket(packet)
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
            pk.attributes = networkAttributes
            pk.runtimeEntityId = 0 // TODO: Entity runtime id
            sendDataPacket(pk)
        }
    }

    @JvmOverloads
    fun disconnect(reason: String, notify: Boolean = true) {
        session.disconnect(reason)
    }

    fun getDisplayName(): String {
        return info?.getUsername() ?: session.address.toString()
    }

    class InvalidPacketException(message: String) : RuntimeException(message)
}
