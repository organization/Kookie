package be.zvz.kookie.network.mcpe.handler.batch

import be.zvz.kookie.event.server.DataPacketReceiveEvent
import be.zvz.kookie.network.mcpe.NetworkSession
import com.nukkitx.protocol.bedrock.BedrockPacket
import com.nukkitx.protocol.bedrock.BedrockSession
import com.nukkitx.protocol.bedrock.handler.BatchHandler
import com.nukkitx.protocol.bedrock.handler.DefaultBatchHandler
import io.netty.buffer.ByteBuf
import io.netty.util.ReferenceCountUtil
import io.netty.util.internal.logging.InternalLoggerFactory

class DefaultBatchHandler(val session: NetworkSession) : BatchHandler {
    private val log = InternalLoggerFactory.getInstance(DefaultBatchHandler::class.java)
    override fun handle(session: BedrockSession?, compressed: ByteBuf?, packets: MutableCollection<BedrockPacket>?) {
        for (packet in packets!!) {
            if (session!!.isLogging && log.isTraceEnabled) {
                log.trace("Inbound {}: {}", session.address, packet)
            }
            val handler = session.packetHandler
            var release = true
            try {
                val ev = DataPacketReceiveEvent(this.session, packet)
                ev.call()
                if (ev.isCancelled) {
                    release = false
                    continue
                }
                if (handler != null && packet.handle(handler)) {
                    release = false
                } else {
                    log.debug(
                        "Unhandled packet for {}: {}",
                        session.address,
                        packet
                    )
                }
            } finally {
                if (release) {
                    ReferenceCountUtil.release(packet)
                }
            }
        }
    }
}
