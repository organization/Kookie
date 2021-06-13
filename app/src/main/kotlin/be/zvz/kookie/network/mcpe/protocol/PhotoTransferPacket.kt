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

@ProtocolIdentify(ProtocolInfo.IDS.PHOTO_TRANSFER_PACKET)
class PhotoTransferPacket : DataPacket(), ClientboundPacket {

    lateinit var photoName: String
    lateinit var photoData: String

    // photos are stored in a sibling directory to the games folder (screenshots/(some UUID)/bookID/example.png)
    lateinit var bookId: String

    override fun decodePayload(input: PacketSerializer) {
        photoName = input.getString()
        photoData = input.getString()
        bookId = input.getString()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putString(photoName)
        output.putString(photoData)
        output.putString(bookId)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handlePhotoTransfer(this)
    }
}
