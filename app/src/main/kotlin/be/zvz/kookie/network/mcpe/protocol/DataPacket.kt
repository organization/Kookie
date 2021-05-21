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

import be.zvz.kookie.network.mcpe.serializer.PacketSerializer
import be.zvz.kookie.utils.BinaryDataException
import com.fasterxml.jackson.module.afterburner.AfterburnerModule
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder

@ProtocolIdentify(networkId = ProtocolInfo.IDS.UNKNOWN)
abstract class DataPacket : Packet {

    var senderSubId = 0

    var recipientSubId = 0

    override fun canBeSentBeforeLogin(): Boolean {
        return false
    }

    override fun getName(): String = this::class.java.simpleName

    final override fun decode(input: PacketSerializer) {
        try {
            decodeHeader(input)
        } catch (e: RuntimeException) {
            when (e) {
                is BinaryDataException,
                is PacketDecodeException -> throw PacketDecodeException.wrap(e, getName())
                else -> throw e
            }
        }
    }

    protected fun decodeHeader(input: PacketSerializer) {
        val header = input.getUnsignedVarInt()
        val pid = header and PID_MASK
        val networkId = this::class.java.getAnnotation(ProtocolIdentify::class.java)?.networkId
        if (pid != networkId?.id) {
            throw IllegalStateException("Expected $networkId for packet ID, got $pid")
        }
        senderSubId = (header shr SENDER_SUBCLIENT_ID_SHIFT) and SUBCLIENT_ID_MASK
        recipientSubId = (header shr RECIPIENT_SUBCLIENT_ID_SHIFT) and SUBCLIENT_ID_MASK
    }

    protected abstract fun decodePayload(input: PacketSerializer)

    final override fun encode(output: PacketSerializer) {
        encodeHeader(output)
        encodePayload(output)
    }

    protected fun encodeHeader(output: PacketSerializer) {
        val networkId = this::class.java.getAnnotation(ProtocolIdentify::class.java)?.networkId!!
        val v = (networkId.id or (senderSubId shl SENDER_SUBCLIENT_ID_SHIFT)) or
            (recipientSubId shl RECIPIENT_SUBCLIENT_ID_SHIFT)
        output.putUnsignedVarInt(v)
    }

    abstract fun encodePayload(output: PacketSerializer)

    companion object {
        internal val jsonMapper = jacksonMapperBuilder()
            .addModule(AfterburnerModule())
            .build()
        const val PID_MASK = 0x3ff
        const val SUBCLIENT_ID_MASK = 0x03
        const val SENDER_SUBCLIENT_ID_SHIFT = 10
        const val RECIPIENT_SUBCLIENT_ID_SHIFT = 12
    }
}
