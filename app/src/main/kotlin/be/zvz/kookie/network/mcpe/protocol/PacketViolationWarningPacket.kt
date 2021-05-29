package be.zvz.kookie.network.mcpe.protocol

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

class PacketViolationWarningPacket : DataPacket(), ServerboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.PACKET_VIOLATION_WARNING_PACKET)

    const val TYPE_MALFORMED = 0

    const val SEVERITY_WARNING = 0
    const val SEVERITY_FINAL_WARNING = 1
    const val SEVERITY_TERMINATING_CONNECTION = 2

    var type: Int
    var severity: Int
    var packetId: Int
    var message: string

    static
    fun create(type: Int, severity: Int, packetId: Int, message: string): self {
        result = new self

                result.type = type
        result.severity = severity
        result.packetId = packetId
        result.message = message

        return result
    }

    fun getType(): Int {
        return type
    }

    fun getSeverity(): Int {
        return severity
    }

    fun getPacketId(): Int {
        return packetId
    }

    fun getMessage(): string {
        return message
    }

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
}
