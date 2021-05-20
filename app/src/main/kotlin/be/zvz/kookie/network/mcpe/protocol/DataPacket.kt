package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(id = -1)
abstract class DataPacket : Packet {

    var senderSubId = 0

    var recipientSubId = 0

    override fun canBeSentBeforeLogin(): Boolean {
        return false
    }

    override fun decode(input: PacketSerializer) {
        try {
            decodeHeader(input)
        } catch (ignore: Exception) {
        }
    }

    fun decodeHeader(input: PacketSerializer) {
        val header = input.getUnsignedVarInt()
        val pid = header and DataPacket.PID_MASK
        senderSubId = (header shr SENDER_SUBCLIENT_ID_SHIFT) and SUBCLIENT_ID_MASK
        recipientSubId = (header shr RECIPIENT_SUBCLIENT_ID_SHIFT) and SUBCLIENT_ID_MASK
    }

    abstract fun decodePayload(input: PacketSerializer)

    override fun encode(output: PacketSerializer) {
        encodeHeader(output)
        encodePayload(output)
    }

    fun encodeHeader(output: PacketSerializer) {
        val pid = pid()
        val v = (pid.id or (senderSubId shl SENDER_SUBCLIENT_ID_SHIFT)) or
            (recipientSubId shl RECIPIENT_SUBCLIENT_ID_SHIFT)
        output.putUnsignedVarInt(v)
    }

    abstract fun encodePayload(output: PacketSerializer)

    companion object {
        const val PID_MASK = 0x3ff
        const val SUBCLIENT_ID_MASK = 0x03
        const val SENDER_SUBCLIENT_ID_SHIFT = 10
        const val RECIPIENT_SUBCLIENT_ID_SHIFT = 12
    }
}
