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
package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.PACKET_VIOLATION_WARNING_PACKET)
class PacketViolationWarningPacket : DataPacket(), ServerboundPacket {

    var type: Int = 0
    var severity: Int = 0
    var packetId: Int = 0
    lateinit var message: String

    override fun decodePayload(input: PacketSerializer) {
        type = input.getVarInt()
        severity = input.getVarInt()
        packetId = input.getVarInt()
        message = input.getString()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putVarInt(type)
        output.putVarInt(severity)
        output.putVarInt(packetId)
        output.putString(message)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handlePacketViolationWarning(this)
    }

    companion object {
        const val TYPE_MALFORMED = 0

        const val SEVERITY_WARNING = 0
        const val SEVERITY_FINAL_WARNING = 1
        const val SEVERITY_TERMINATING_CONNECTION = 2

        @JvmStatic
        fun create(type: Int, severity: Int, packetId: Int, message: String) = PacketViolationWarningPacket().apply {
            this.type = type
            this.severity = severity
            this.packetId = packetId
            this.message = message
        }
    }
}
