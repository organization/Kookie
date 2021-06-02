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
package be.zvz.kookie.network.mcpe.serializer

import be.zvz.kookie.network.mcpe.protocol.Packet
import be.zvz.kookie.network.mcpe.protocol.PacketPool

class PacketBatch(private val buffer: String = "") {

    fun getPackets(): MutableMap<Packet, String> {
        val serializer = PacketSerializer(buffer)
        val list: MutableMap<Packet, String> = mutableMapOf()
        try {
            while (!serializer.feof()) {
                val buf = serializer.getString()
                PacketPool.instance?.let {
                    list.put(it.getPacket(buf), buf)
                }
            }
        } catch (e: Exception) {
        }
        return list
    }

    fun getBuffer(): String = buffer

    companion object {
        fun fromPackets(packets: MutableList<Packet>): PacketBatch {
            val serializer = PacketSerializer()
            packets.forEach {
                val subSerializer = PacketSerializer()
                it.encode(subSerializer)
                serializer.putString(subSerializer.buffer.toString())
            }
            return PacketBatch(serializer.buffer.toString())
        }
    }
}
