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
package be.zvz.kookie.network.mcpe.protocol.serializer

import be.zvz.kookie.network.mcpe.protocol.Packet
import be.zvz.kookie.network.mcpe.protocol.PacketPool

class PacketBatch @JvmOverloads constructor(private val buffer: String = "") {

    fun getPackets(packetPool: PacketPool, max: Int) = sequence {
        val serializer = PacketSerializer(buffer)
        var c = 0
        while (c < max && !serializer.feof()) {
            val buf = serializer.getString()
            c++
            yield(Triple(c, packetPool.getPacket(buf), buf))
        }
    }

    fun getBuffer(): String = buffer

    companion object {
        @JvmStatic
        fun fromPackets(vararg packets: Packet): PacketBatch {
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
