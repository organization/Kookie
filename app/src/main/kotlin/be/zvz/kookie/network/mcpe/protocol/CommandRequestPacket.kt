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

class CommandRequestPacket : DataPacket(), ServerboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.COMMAND_REQUEST_PACKET)

    var command: string
    var originData: CommandOriginData
    var isInternal: Boolean

    override fun decodePayload(input: PacketSerializer) {
        command = input.getString()
        originData = input.getCommandOriginData()
        isInternal = input.getBool()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putString(command)
        output.putCommandOriginData(originData)
        output.putBool(isInternal)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleCommandRequest(this)
    }
}
