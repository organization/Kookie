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
