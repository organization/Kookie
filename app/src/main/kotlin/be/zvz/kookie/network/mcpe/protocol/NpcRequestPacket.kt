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

@ProtocolIdentify(ProtocolInfo.IDS.NPC_REQUEST_PACKET)
class NpcRequestPacket : DataPacket(), ServerboundPacket {

    var entityRuntimeId: Long = 0
    var requestType: Int = 0
    lateinit var commandString: String
    var actionType: Int = 0
    lateinit var sceneName: String

    override fun decodePayload(input: PacketSerializer) {
        entityRuntimeId = input.getEntityRuntimeId()
        requestType = input.getByte()
        commandString = input.getString()
        actionType = input.getByte()
        sceneName = input.getString()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityRuntimeId(entityRuntimeId)
        output.putByte(requestType)
        output.putString(commandString)
        output.putByte(actionType)
        output.putString(sceneName)
    }

    companion object {
        const val REQUEST_SET_ACTIONS = 0
        const val REQUEST_EXECUTE_ACTION = 1
        const val REQUEST_EXECUTE_CLOSING_COMMANDS = 2
        const val REQUEST_SET_NAME = 3
        const val REQUEST_SET_SKIN = 4
        const val REQUEST_SET_INTERACTION_TEXT = 5
        const val REQUEST_EXECUTE_OPENING_COMMANDS = 6
    }
}
