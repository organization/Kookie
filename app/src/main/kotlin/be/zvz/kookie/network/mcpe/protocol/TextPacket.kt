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

@ProtocolIdentify(ProtocolInfo.IDS.TEXT_PACKET)
class TextPacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    var type: Int = -1
    var needTranslation: Boolean = false
    var sourceName: String = ""
    var message: String = ""
    var parameters: MutableList<String> = mutableListOf()
    var xboxUserId: String = ""
    var platformChatId: String = ""

    override fun decodePayload(input: PacketSerializer) {
        type = input.getByte()
        needTranslation = input.getBoolean()
        if (type in TYPE_CHAT..TYPE_ANNOUNCEMENT) {
            sourceName = input.getString()
        }
        if (type in TYPE_CHAT..TYPE_JSON) {
            message = input.getString()
        }
        if (type in TYPE_TRANSLATION..TYPE_JUKEBOX_POPUP) {
            message = input.getString()
            repeat(input.getUnsignedVarInt()) {
                parameters.add(input.getString())
            }
        }
        xboxUserId = input.getString()
        platformChatId = input.getString()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(type)
        output.putBoolean(needTranslation)
        if (type in TYPE_CHAT..TYPE_ANNOUNCEMENT) {
            output.putString(sourceName)
        }
        if (type in TYPE_CHAT..TYPE_JSON) {
            output.putString(message)
        }
        if (type in TYPE_TRANSLATION..TYPE_JUKEBOX_POPUP) {
            output.putString(message)
            output.putUnsignedVarInt(parameters.size)
            parameters.forEach(output::putString)
        }
        output.putString(xboxUserId)
        output.putString(platformChatId)
    }

    companion object {
        const val TYPE_RAW = 0
        const val TYPE_CHAT = 1
        const val TYPE_TRANSLATION = 2
        const val TYPE_POPUP = 3
        const val TYPE_JUKEBOX_POPUP = 4
        const val TYPE_TIP = 5
        const val TYPE_SYSTEM = 6
        const val TYPE_WHISPER = 7
        const val TYPE_ANNOUNCEMENT = 8
        const val TYPE_JSON_WHISPER = 9
        const val TYPE_JSON = 10

        private fun messageOnly(type: Int, message: String) = TextPacket().apply {
            this.type = type
            this.message = message
        }

        private fun baseTranslation(type: Int, key: String, parameters: MutableList<String>): TextPacket =
            messageOnly(type, key).apply {
                this.needTranslation = true
                this.parameters = parameters
            }

        @JvmStatic fun raw(message: String): TextPacket =
            messageOnly(TYPE_CHAT, message)

        @JvmStatic fun translation(key: String, parameters: MutableList<String>): TextPacket =
            baseTranslation(TYPE_TRANSLATION, key, parameters)

        @JvmStatic fun popup(message: String): TextPacket =
            messageOnly(TYPE_POPUP, message)

        @JvmStatic fun translatedPopup(key: String, parameters: MutableList<String>): TextPacket =
            baseTranslation(TYPE_POPUP, key, parameters)

        @JvmStatic fun jukeboxPopup(key: String, parameters: MutableList<String>): TextPacket =
            baseTranslation(TYPE_JUKEBOX_POPUP, key, parameters)

        @JvmStatic fun tip(message: String): TextPacket =
            messageOnly(TYPE_TIP, message)
    }
}
