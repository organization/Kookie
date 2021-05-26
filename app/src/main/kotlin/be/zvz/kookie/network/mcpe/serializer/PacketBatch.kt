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
