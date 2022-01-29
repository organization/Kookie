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

@ProtocolIdentify(ProtocolInfo.IDS.EDUCATION_SETTINGS_PACKET)
class EducationSettingsPacket : DataPacket(), ClientboundPacket {

    lateinit var codeBuilderDefaultUri: String
    lateinit var codeBuilderTitle: String
    var canResizeCodeBuilder: Boolean = false
    var codeBuilderOverrideUri: String? = null
    var hasQuiz: Boolean = false

    override fun decodePayload(input: PacketSerializer) {
        codeBuilderDefaultUri = input.getString()
        codeBuilderTitle = input.getString()
        canResizeCodeBuilder = input.getBoolean()
        codeBuilderOverrideUri = if (input.getBoolean()) {
            input.getString()
        } else {
            null
        }
        hasQuiz = input.getBoolean()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putString(codeBuilderDefaultUri)
        output.putString(codeBuilderTitle)
        output.putBoolean(canResizeCodeBuilder)
        output.putBoolean(codeBuilderOverrideUri !== null)
        codeBuilderOverrideUri?.let(output::putString)
        output.putBoolean(hasQuiz)
    }

    companion object {
        @JvmStatic
        fun create(
            codeBuilderDefaultUri: String,
            codeBuilderTitle: String,
            canResizeCodeBuilder: Boolean,
            codeBuilderOverrideUri: String?,
            hasQuiz: Boolean
        ) = EducationSettingsPacket().apply {
            this.codeBuilderDefaultUri = codeBuilderDefaultUri
            this.codeBuilderTitle = codeBuilderTitle
            this.canResizeCodeBuilder = canResizeCodeBuilder
            this.codeBuilderOverrideUri = codeBuilderOverrideUri
            this.hasQuiz = hasQuiz
        }
    }
}
