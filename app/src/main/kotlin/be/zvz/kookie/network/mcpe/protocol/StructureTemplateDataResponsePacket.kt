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
import be.zvz.kookie.network.mcpe.protocol.types.CacheableNbt

@ProtocolIdentify(ProtocolInfo.IDS.STRUCTURE_TEMPLATE_DATA_RESPONSE_PACKET)
class StructureTemplateDataResponsePacket : DataPacket(), ClientboundPacket {

    lateinit var structureTemplateName: String

    var namedtag: CacheableNbt? = null

    override fun decodePayload(input: PacketSerializer) {
        structureTemplateName = input.getString()
        if (input.getBoolean()) {
            namedtag = CacheableNbt(input.getNbtCompoundRoot())
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putString(structureTemplateName)
        output.putBoolean(namedtag != null)
        namedtag?.let { output.put(it.encodedNbt) }
    }
}
