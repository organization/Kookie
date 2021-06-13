package be.zvz.kookie.network.mcpe.protocol.serializer

import be.zvz.kookie.network.mcpe.protocol.Packet
import be.zvz.kookie.network.mcpe.protocol.PacketPool

class PacketBatch(private val buffer: String = "") {

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
