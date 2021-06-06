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

@ProtocolIdentify(ProtocolInfo.IDS.TRANSFER_PACKET)
class TransferPacket : DataPacket(), ClientboundPacket {

    lateinit var address: String
    var port: Int = 19132

    override fun decodePayload(input: PacketSerializer) {
        address = input.getString()
        port = input.getLShort()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putString(address)
        output.putLShort(port)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleTransfer(this)
    }

    companion object {
        fun create(address: String, port: Int): TransferPacket {
            return TransferPacket().apply {
                this.address = address
                this.port = port
            }
        }
    }
}
