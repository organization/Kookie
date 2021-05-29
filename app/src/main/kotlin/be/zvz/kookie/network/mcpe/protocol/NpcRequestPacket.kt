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

class NpcRequestPacket : DataPacket(), ServerboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.NPC_REQUEST_PACKET)

    const val REQUEST_SET_ACTIONS = 0
    const val REQUEST_EXECUTE_ACTION = 1
    const val REQUEST_EXECUTE_CLOSING_COMMANDS = 2
    const val REQUEST_SET_NAME = 3
    const val REQUEST_SET_SKIN = 4
    const val REQUEST_SET_INTERACTION_TEXT = 5

    var entityRuntimeId: Int
    var requestType: Int
    var commandString: string
    var actionType: Int

    override fun decodePayload(input: PacketSerializer) {
        entityRuntimeId = input.getEntityRuntimeId()
        requestType = input.getByte()
        commandString = input.getString()
        actionType = input.getByte()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityRuntimeId(entityRuntimeId)
        output.putByte(requestType)
        output.putString(commandString)
        output.putByte(actionType)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleNpcRequest(this)
    }
}
