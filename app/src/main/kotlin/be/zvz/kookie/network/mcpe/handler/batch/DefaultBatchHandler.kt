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
 * Copyright (C) 2021 - 2022 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
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
