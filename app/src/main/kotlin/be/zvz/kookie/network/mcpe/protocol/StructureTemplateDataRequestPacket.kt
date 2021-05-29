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

class StructureTemplateDataRequestPacket : DataPacket(), ServerboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.STRUCTURE_TEMPLATE_DATA_REQUEST_PACKET)

    const val TYPE_ALWAYS_LOAD = 1
    const val TYPE_CREATE_AND_LOAD = 2

    var structureTemplateName: string
    var structureBlockX: Int
    var structureBlockY: Int
    var structureBlockZ: Int
    var structureSettings: StructureSettings
    var structureTemplateResponseType: Int

    override fun decodePayload(input: PacketSerializer) {
        structureTemplateName = input.getString()
        input.getBlockPosition(structureBlockX, structureBlockY, structureBlockZ)
        structureSettings = input.getStructureSettings()
        structureTemplateResponseType = input.getByte()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putString(structureTemplateName)
        output.putBlockPosition(structureBlockX, structureBlockY, structureBlockZ)
        output.putStructureSettings(structureSettings)
        output.putByte(structureTemplateResponseType)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleStructureTemplateDataRequest(this)
    }
}
