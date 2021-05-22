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
import be.zvz.kookie.network.mcpe.protocol.types.login.JwtChain
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer
import be.zvz.kookie.utils.BinaryStream
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.readValue

@ProtocolIdentify(ProtocolInfo.IDS.LOGIN_PACKET)
class LoginPacket : DataPacket(), ServerboundPacket {

    lateinit var clientDataJwt: String
    lateinit var chainDataJwt: JwtChain

    var protocol: Int = 0

    override fun canBeSentBeforeLogin(): Boolean {
        return true
    }

    override fun decodePayload(input: PacketSerializer) {
        protocol = input.getInt()
        decodeConnectionRequest(input.getString())
    }

    protected fun decodeConnectionRequest(binary: String) {
        val connectionReqReader = BinaryStream(binary)

        val chainData: JwtChain
        try {
            chainData = jsonMapper.readValue(connectionReqReader.get(connectionReqReader.getLInt()))
        } catch (e: JsonProcessingException) {
            throw PacketDecodeException("Failed decoding chain data JSON", e)
        }

        chainDataJwt = chainData
        clientDataJwt = connectionReqReader.get(connectionReqReader.getLInt())
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putInt(protocol)
        output.putString(encodeConnectionRequest())
    }

    protected fun encodeConnectionRequest(): String {
        val connectionReqWriter = BinaryStream()
        val chainDataJson: String
        try {
            chainDataJson = jsonMapper.writeValueAsString(chainDataJwt)
        } catch (e: JsonProcessingException) {
            throw PacketDecodeException("Failed encoding chain data JSON", e)
        }
        connectionReqWriter.putLInt(chainDataJson.length)
        connectionReqWriter.put(chainDataJson)
        connectionReqWriter.putLInt(clientDataJwt.length)
        connectionReqWriter.put(clientDataJwt)

        return connectionReqWriter.buffer.toString()
    }

    override fun handle(handler: PacketHandlerInterface) {
    }
}
