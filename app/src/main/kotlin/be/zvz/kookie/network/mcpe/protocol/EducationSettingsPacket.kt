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

class EducationSettingsPacket : DataPacket(), ClientboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.EDUCATION_SETTINGS_PACKET)

    var codeBuilderDefaultUri: string
    var codeBuilderTitle: string
    var canResizeCodeBuilder: Boolean
    var codeBuilderOverrideUri: string|null
    var hasQuiz: Boolean

    static
    fun create(
        codeBuilderDefaultUri: string,
        codeBuilderTitle: string,
        canResizeCodeBuilder: Boolean,
        ?
        codeBuilderOverrideUri: string,
        hasQuiz: Boolean
    ): self {
        result = new self
                result.codeBuilderDefaultUri = codeBuilderDefaultUri
        result.codeBuilderTitle = codeBuilderTitle
        result.canResizeCodeBuilder = canResizeCodeBuilder
        result.codeBuilderOverrideUri = codeBuilderOverrideUri
        result.hasQuiz = hasQuiz
        return result
    }

    fun getCodeBuilderDefaultUri(): string {
        return codeBuilderDefaultUri
    }

    fun getCodeBuilderTitle(): string {
        return codeBuilderTitle
    }

    fun canResizeCodeBuilder(): Boolean {
        return canResizeCodeBuilder
    }

    fun getCodeBuilderOverrideUri(): ?string
    {
        return codeBuilderOverrideUri
    }

    fun getHasQuiz(): Boolean {
        return hasQuiz
    }

    override fun decodePayload(input: PacketSerializer) {
        codeBuilderDefaultUri = input.getString()
        codeBuilderTitle = input.getString()
        canResizeCodeBuilder = input.getBool()
        if (input.getBool()) {
            codeBuilderOverrideUri = input.getString()
        } else {
            codeBuilderOverrideUri = null
        }
        hasQuiz = input.getBool()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putString(codeBuilderDefaultUri)
        output.putString(codeBuilderTitle)
        output.putBool(canResizeCodeBuilder)
        output.putBool(codeBuilderOverrideUri !== null)
        if (codeBuilderOverrideUri !== null) {
            output.putString(codeBuilderOverrideUri)
        }
        output.putBool(hasQuiz)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleEducationSettings(this)
    }
}
