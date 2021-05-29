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

class SetTitlePacket : DataPacket(), ClientboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.SET_TITLE_PACKET)

    const val TYPE_CLEAR_TITLE = 0
    const val TYPE_RESET_TITLE = 1
    const val TYPE_SET_TITLE = 2
    const val TYPE_SET_SUBTITLE = 3
    const val TYPE_SET_ACTIONBAR_MESSAGE = 4
    const val TYPE_SET_ANIMATION_TIMES = 5
    const val TYPE_SET_TITLE_JSON = 6
    const val TYPE_SET_SUBTITLE_JSON = 7
    const val TYPE_SET_ACTIONBAR_MESSAGE_JSON = 8

    var type: Int
    var text: string = ""
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

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleSetTitle(this)
    }

    static
    fun type(type: Int): self {
        result = new self
                result.type = type
        return result
    }

    static
    fun text(type: Int, text: string): self {
        result = type(type)
        result.text = text
        return result
    }

    static
    fun title(text: string): self {
        return text(TYPE_SET_TITLE, text)
    }

    static
    fun subtitle(text: string): self {
        return text(TYPE_SET_SUBTITLE, text)
    }

    static
    fun actionBarMessage(text: string): self {
        return text(TYPE_SET_ACTIONBAR_MESSAGE, text)
    }

    static
    fun clearTitle(): self {
        return type(TYPE_CLEAR_TITLE)
    }

    static
    fun resetTitleOptions(): self {
        return type(TYPE_RESET_TITLE)
    }

    static
    fun setAnimationTimes(fadeIn: Int, stay: Int, fadeOut: Int): self {
        result = type(TYPE_SET_ANIMATION_TIMES)
        result.fadeInTime = fadeIn
        result.stayTime = stay
        result.fadeOutTime = fadeOut
        return result
    }
}
