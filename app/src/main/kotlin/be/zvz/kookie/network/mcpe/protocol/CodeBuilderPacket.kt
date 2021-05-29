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

class CodeBuilderPacket : DataPacket(), ClientboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.CODE_BUILDER_PACKET)

    var url: string
    var openCodeBuilder: Boolean

    static
    fun create(url: string, openCodeBuilder: Boolean): self {
        result = new self
                result.url = url
        result.openCodeBuilder = openCodeBuilder
        return result
    }

    fun getUrl(): string {
        return url
    }

    fun openCodeBuilder(): Boolean {
        return openCodeBuilder
    }

    override fun decodePayload(input: PacketSerializer) {
        url = input.getString()
        openCodeBuilder = input.getBool()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putString(url)
        output.putBool(openCodeBuilder)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleCodeBuilder(this)
    }
}
