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
import be.zvz.kookie.network.mcpe.protocol.types.StructureSettings

@ProtocolIdentify(ProtocolInfo.IDS.STRUCTURE_TEMPLATE_DATA_REQUEST_PACKET)
class StructureTemplateDataRequestPacket : DataPacket(), ServerboundPacket {

    lateinit var structureTemplateName: String

    var blockPos: PacketSerializer.BlockPosition = PacketSerializer.BlockPosition()

    lateinit var structureSettings: StructureSettings
    var structureTemplateResponseType: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        structureTemplateName = input.getString()
        blockPos = input.getBlockPosition()
        structureSettings = input.getStructureSettings()
        structureTemplateResponseType = input.getByte()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putString(structureTemplateName)
        output.putBlockPosition(blockPos)
        output.putStructureSettings(structureSettings)
        output.putByte(structureTemplateResponseType)
    }

    enum class Type(val value: Int) {
        ALWAYS_LOAD(1),
        CREATE_AND_LOAD(2),
    }
}
