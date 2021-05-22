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
package be.zvz.kookie.network.mcpe.protocol

class PacketPool {

    protected val pool: MutableMap<Int, Packet> = mutableMapOf()

    init {
        registerPacket(UnknownPacket())
        registerPacket(LoginPacket())
        registerPacket(PlayStatusPacket())
        registerPacket(ResourcePacksInfoPacket())
        registerPacket(ResourcePackStackPacket())
    }

    fun registerPacket(packet: Packet) {
        val protocolIdentify = packet::class.java.getAnnotation(ProtocolIdentify::class.java)!!
        pool[protocolIdentify.networkId.id] = packet
    }

    fun getPacketByPid(pid: Int): Packet {
        val packet = pool[pid] ?: UnknownPacket()
        return packet.clone()
    }

    companion object {
        var instance: PacketPool? = null
            get() {
                if (field == null) {
                    field = PacketPool()
                }
                return field
            }
            set(value: PacketPool?) {
                if (value == null) {
                    throw RuntimeException("Cannot set instance to null")
                }
                field = value
            }
    }
}
