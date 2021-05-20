package be.zvz.kookie.network.mcpe.protocol

class PacketDecodeException private constructor(msg: String, cause: Throwable) : RuntimeException(msg, cause) {
    companion object {
        @JvmStatic
        fun wrap(previous: Throwable, prefix: String? = null): PacketDecodeException {
            return PacketDecodeException(
                if (prefix !== null) {
                    "$prefix: "
                } else {
                    ""
                } + previous.message,
                previous
            )
        }
    }
}
