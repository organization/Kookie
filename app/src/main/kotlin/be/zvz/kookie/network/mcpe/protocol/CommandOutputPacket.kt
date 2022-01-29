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

import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.command.CommandOriginData
import be.zvz.kookie.network.mcpe.protocol.types.command.CommandOutputMessage
import be.zvz.kookie.utils.BinaryDataException

@ProtocolIdentify(ProtocolInfo.IDS.COMMAND_OUTPUT_PACKET)
open class CommandOutputPacket : DataPacket(), ClientboundPacket {
    lateinit var originData: CommandOriginData
    var outputType: Type = Type.UNKNOWN
    var successCount: Int = 0
    val messages = mutableListOf<CommandOutputMessage>()
    lateinit var unknownString: String

    override fun decodePayload(input: PacketSerializer) {
        originData = input.getCommandOriginData()
        outputType = Type.from(input.getByte())
        successCount = input.getUnsignedVarInt()

        repeat(input.getUnsignedVarInt()) {
            messages.add(getCommandMessage(input))
        }

        if (outputType === Type.DATA_SET) {
            unknownString = input.getString()
        }
    }

    /**
     * @throws BinaryDataException
     */
    protected open fun getCommandMessage(input: PacketSerializer): CommandOutputMessage {
        val message = CommandOutputMessage()

        message.isInternal = input.getBoolean()
        message.messageId = input.getString()

        repeat(input.getUnsignedVarInt()) {
            message.parameters.add(input.getString())
        }

        return message
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putCommandOriginData(originData)
        output.putByte(outputType.id)
        output.putUnsignedVarInt(successCount)

        output.putUnsignedVarInt(messages.size)
        messages.forEach { message ->
            putCommandMessage(message, output)
        }

        if (outputType === Type.DATA_SET) {
            output.putString(unknownString)
        }
    }

    protected open fun putCommandMessage(message: CommandOutputMessage, output: PacketSerializer) {
        output.putBoolean(message.isInternal)
        output.putString(message.messageId)

        output.putUnsignedVarInt(message.parameters.size)
        message.parameters.forEach(output::putString)
    }

    enum class Type(val id: Int) {
        LAST(1),
        SILENT(2),
        ALL(3),
        DATA_SET(4),
        UNKNOWN(-1);

        companion object {
            @JvmStatic
            fun from(findValue: Int): Type = values().firstOrNull { it.id == findValue } ?: UNKNOWN
        }
    }
}
