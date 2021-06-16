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

@ProtocolIdentify(ProtocolInfo.IDS.BOOK_EDIT_PACKET)
class BookEditPacket : DataPacket(), ServerboundPacket {
    var type: Int = 0
    var inventorySlot: Int = 0
    var pageNumber: Int = 0
    var secondaryPageNumber: Int = 0

    lateinit var text: String
    lateinit var photoName: String

    lateinit var title: String
    lateinit var author: String
    lateinit var xuid: String

    override fun decodePayload(input: PacketSerializer) {
        type = input.getByte()
        inventorySlot = input.getByte()

        when (type) {
            TYPE_REPLACE_PAGE, TYPE_ADD_PAGE -> {
                pageNumber = input.getByte()
                text = input.getString()
                photoName = input.getString()
            }
            TYPE_DELETE_PAGE -> {
                pageNumber = input.getByte()
            }
            TYPE_SWAP_PAGES -> {
                pageNumber = input.getByte()
                secondaryPageNumber = input.getByte()
            }
            TYPE_SIGN_BOOK -> {
                title = input.getString()
                author = input.getString()
                xuid = input.getString()
            }
            else -> throw PacketDecodeException("Unknown book edit type type!")
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(type)
        output.putByte(inventorySlot)

        when (type) {
            TYPE_REPLACE_PAGE, TYPE_ADD_PAGE -> {
                output.putByte(pageNumber)
                output.putString(text)
                output.putString(photoName)
            }
            TYPE_DELETE_PAGE -> output.putByte(pageNumber)
            TYPE_SWAP_PAGES -> {
                output.putByte(pageNumber)
                output.putByte(secondaryPageNumber)
            }
            TYPE_SIGN_BOOK -> {

                output.putString(title)
                output.putString(author)
                output.putString(xuid)
            }
            else -> throw IllegalArgumentException("Unknown book edit type type!")
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleBookEdit(this)

    companion object {
        const val TYPE_REPLACE_PAGE = 0
        const val TYPE_ADD_PAGE = 1
        const val TYPE_DELETE_PAGE = 2
        const val TYPE_SWAP_PAGES = 3
        const val TYPE_SIGN_BOOK = 4
    }
}
