package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.serializer.PacketSerializer
import be.zvz.kookie.utils.BinaryDataException

@ProtocolIdentify(networkId = -1)
abstract class DataPacket : Packet {

    var senderSubId = 0

    var recipientSubId = 0

    override fun canBeSentBeforeLogin(): Boolean {
        return false
    }

    final override fun decode(input: PacketSerializer) {
        try {
            decodeHeader(input)
        } catch (e: RuntimeException) {
            when (e) {
                is BinaryDataException,
                is PacketDecodeException -> throw PacketDecodeException.wrap(e, getName())
                else -> throw e
            }
        }
    }

    protected fun decodeHeader(input: PacketSerializer) {
        val header = input.getUnsignedVarInt()
        val pid = header and PID_MASK
        val networkId = this::class.java.getAnnotation(ProtocolIdentify::class.java)?.networkId
        if (pid != networkId) {
            throw IllegalStateException("Expected $networkId for packet ID, got $pid")
        }
        senderSubId = (header shr SENDER_SUBCLIENT_ID_SHIFT) and SUBCLIENT_ID_MASK
        recipientSubId = (header shr RECIPIENT_SUBCLIENT_ID_SHIFT) and SUBCLIENT_ID_MASK
    }

    protected abstract fun decodePayload(input: PacketSerializer)

    final override fun encode(output: PacketSerializer) {
        encodeHeader(output)
        encodePayload(output)
    }

    protected fun encodeHeader(output: PacketSerializer) {
        val networkId = this::class.java.getAnnotation(ProtocolIdentify::class.java)?.networkId!!
        val v = (networkId or (senderSubId shl SENDER_SUBCLIENT_ID_SHIFT)) or
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
