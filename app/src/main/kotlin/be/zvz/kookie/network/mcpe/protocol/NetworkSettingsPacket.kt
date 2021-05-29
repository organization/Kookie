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

class NetworkSettingsPacket : DataPacket(), ClientboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.NETWORK_SETTINGS_PACKET)

    const val COMPRESS_NOTHING = 0
    const val COMPRESS_EVERYTHING = 1

    var compressionThreshold: Int

    static
    fun create(compressionThreshold: Int): self {
        result = new self
                result.compressionThreshold = compressionThreshold
        return result
    }

    fun getCompressionThreshold(): Int {
        return compressionThreshold
    }

    override fun decodePayload(input: PacketSerializer) {
        compressionThreshold = input.getLShort()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putLShort(compressionThreshold)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleNetworkSettings(this)
    }
}
