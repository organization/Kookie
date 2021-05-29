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

class FilterTextPacket : DataPacket(), ClientboundPacket, ServerboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.FILTER_TEXT_PACKET)

    var text: string
    var fromServer: Boolean

    static
    fun create(text: string, server: Boolean): self {
        result = new self
                result.text = text
        result.fromServer = server
        return result
    }

    fun getText(): string {
        return text
    }

    fun isFromServer(): Boolean {
        return fromServer
    }

    override fun decodePayload(input: PacketSerializer) {
        text = input.getString()
        fromServer = input.getBool()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putString(text)
        output.putBool(fromServer)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleFilterText(this)
    }
}
