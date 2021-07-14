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

@ProtocolIdentify(ProtocolInfo.IDS.NPC_DIALOGUE_PACKET)
class NpcDialoguePacket : DataPacket(), ClientboundPacket {

    enum class Actions(val type: Int) {
        OPEN(0),
        CLOSE(1)
    }

    var npcActorUniqueId: Long = -1
    lateinit var actionType: Actions
    lateinit var dialogues: String
    lateinit var sceneName: String
    lateinit var npcName: String
    lateinit var actionJson: String

    override fun decodePayload(input: PacketSerializer) {
        npcActorUniqueId = input.getEntityUniqueId()
        actionType = when (input.getVarInt()) {
            0 -> Actions.OPEN
            1 -> Actions.CLOSE
            else -> throw PacketDecodeException("Unknown action type given")
        }
        dialogues = input.getString()
        sceneName = input.getString()
        npcName = input.getString()
        actionJson = input.getString()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityUniqueId(npcActorUniqueId)
        output.putVarInt(actionType.type)
        output.putString(dialogues)
        output.putString(sceneName)
        output.putString(npcName)
        output.putString(actionJson)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleNpcDialogue(this)
    }
}
