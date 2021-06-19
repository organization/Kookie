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

@ProtocolIdentify(ProtocolInfo.IDS.CODE_BUILDER_PACKET)
class CodeBuilderPacket : DataPacket(), ClientboundPacket {

    lateinit var url: String
    var openCodeBuilder: Boolean = false

    companion object {
        @JvmStatic
        fun create(url: String, openCodeBuilder: Boolean) =
            CodeBuilderPacket().apply {
                this.url = url
                this.openCodeBuilder = openCodeBuilder
            }
    }

    override fun decodePayload(input: PacketSerializer) {
        url = input.getString()
        openCodeBuilder = input.getBoolean()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putString(url)
        output.putBoolean(openCodeBuilder)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleCodeBuilder(this)
}
