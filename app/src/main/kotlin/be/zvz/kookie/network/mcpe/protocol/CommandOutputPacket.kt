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
use fun count

class CommandOutputPacket : DataPacket(), ClientboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.COMMAND_OUTPUT_PACKET)

    const val TYPE_LAST = 1
    const val TYPE_SILENT = 2
    const val TYPE_ALL = 3
    const val TYPE_DATA_SET = 4

    var originData: CommandOriginData
    var outputputType: Int
    var successCount: Int
    /** @var CommandOutputMessage[] */
    messages = []
    var unknownString: string

    override fun decodePayload(input: PacketSerializer) {
        originData = input.getCommandOriginData()
        outputType = input.getByte()
        successCount = input.getUnsignedVarInt()

        for (i = 0, size = input.getUnsignedVarInt() i < size++i){
            messages[] = getCommandMessage(input)
        }

        if (outputType === TYPE_DATA_SET) {
            unknownString = input.getString()
        }
    }

    /**
     * @throws BinaryDataException
     */
    override fun getCommandMessage(input: PacketSerializer): CommandOutputMessage {
        message = new CommandOutputMessage ()

        message.isInternal = input.getBool()
        message.messageId = input.getString()

        for (i = 0, size = input.getUnsignedVarInt() i < size++i){
            message.parameters[] = input.getString()
        }

        return message
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putCommandOriginData(originData)
        output.putByte(outputType)
        output.putUnsignedVarInt(successCount)

        output.putUnsignedVarInt(count(messages))
        foreach(messages message : as) {
            putCommandMessage(message, output)
        }

        if (outputType === TYPE_DATA_SET) {
            output.putString(unknownString)
        }
    }

    override fun putCommandMessage(message: CommandOutputMessage, output: PacketSerializer) {
        output.putBool(message.isInternal)
        output.putString(message.messageId)

        output.putUnsignedVarInt(count(message.parameters))
        foreach(message.parameters parameter : as) {
            output.putString(parameter)
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleCommandOutput(this)
    }
}
