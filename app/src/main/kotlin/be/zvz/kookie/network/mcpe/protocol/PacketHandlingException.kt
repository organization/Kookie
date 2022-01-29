package be.zvz.kookie.network.mcpe.protocol

class PacketHandlingException(override val message: String?) : RuntimeException() {

    companion object {

        fun wrap(e: Throwable, message: String?): PacketHandlingException {
            return PacketHandlingException(message).apply {
                initCause(e)
            }
        }
    }
}
