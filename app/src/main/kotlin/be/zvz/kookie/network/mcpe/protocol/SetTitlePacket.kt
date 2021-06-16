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

@ProtocolIdentify(ProtocolInfo.IDS.SET_TITLE_PACKET)
class SetTitlePacket : DataPacket(), ClientboundPacket {

    var type: Int = 0
    var text: String = ""
    var fadeInTime: Int = 0
    var stayTime: Int = 0
    var fadeOutTime: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        type = input.getVarInt()
        text = input.getString()
        fadeInTime = input.getVarInt()
        stayTime = input.getVarInt()
        fadeOutTime = input.getVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putVarInt(type)
        output.putString(text)
        output.putVarInt(fadeInTime)
        output.putVarInt(stayTime)
        output.putVarInt(fadeOutTime)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleSetTitle(this)

    companion object {
        const val TYPE_CLEAR_TITLE = 0
        const val TYPE_RESET_TITLE = 1
        const val TYPE_SET_TITLE = 2
        const val TYPE_SET_SUBTITLE = 3
        const val TYPE_SET_ACTIONBAR_MESSAGE = 4
        const val TYPE_SET_ANIMATION_TIMES = 5
        const val TYPE_SET_TITLE_JSON = 6
        const val TYPE_SET_SUBTITLE_JSON = 7
        const val TYPE_SET_ACTIONBAR_MESSAGE_JSON = 8

        @JvmStatic
        fun type(type: Int): SetTitlePacket = SetTitlePacket().apply {
            this.type = type
        }

        @JvmStatic
        fun text(type: Int, text: String): SetTitlePacket = type(type).apply {
            this.text = text
        }

        @JvmStatic
        fun title(text: String): SetTitlePacket = text(TYPE_SET_TITLE, text)

        @JvmStatic
        fun subtitle(text: String): SetTitlePacket = text(TYPE_SET_SUBTITLE, text)

        @JvmStatic
        fun actionBarMessage(text: String): SetTitlePacket = text(TYPE_SET_ACTIONBAR_MESSAGE, text)

        @JvmStatic
        fun clearTitle(): SetTitlePacket = type(TYPE_CLEAR_TITLE)

        @JvmStatic
        fun resetTitleOptions(): SetTitlePacket = type(TYPE_RESET_TITLE)

        @JvmStatic
        fun setAnimationTimes(fadeIn: Int, stay: Int, fadeOut: Int): SetTitlePacket =
            type(TYPE_SET_ANIMATION_TIMES).apply {
                this.fadeInTime = fadeIn
                this.stayTime = stay
                this.fadeOutTime = fadeOut
            }
    }
}
