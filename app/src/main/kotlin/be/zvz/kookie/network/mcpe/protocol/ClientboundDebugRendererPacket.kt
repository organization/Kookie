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

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.CLIENTBOUND_DEBUG_RENDERER_PACKET)
class ClientboundDebugRendererPacket : DataPacket(), ClientboundPacket {
    var type: Int = 0

    lateinit var text: String
    lateinit var position: Vector3
    var red: Float = 0f
    var green: Float = 0f
    var blue: Float = 0f
    var alpha: Float = 0f
    var durationMillis: Long = 0

    override fun decodePayload(input: PacketSerializer) {
        type = input.getLInt()

        when (type) {
            TYPE_CLEAR -> {
            }
            TYPE_ADD_CUBE -> {
                text = input.getString()
                position = input.getVector3()
                red = input.getLFloat()
                green = input.getLFloat()
                blue = input.getLFloat()
                alpha = input.getLFloat()
                durationMillis = input.getLLong()
            }
            else -> throw IllegalArgumentException("Unknown type $type")
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putLInt(type)

        when (type) {
            TYPE_CLEAR -> {
            }
            TYPE_ADD_CUBE -> {

                output.putString(text)
                output.putVector3(position)
                output.putLFloat(red)
                output.putLFloat(green)
                output.putLFloat(blue)
                output.putLFloat(alpha)
                output.putLLong(durationMillis)
            }
            else -> throw IllegalArgumentException("Unknown type $type")
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleClientboundDebugRenderer(this)
    }

    companion object {
        const val TYPE_CLEAR = 1
        const val TYPE_ADD_CUBE = 2

        fun base(type: Int): ClientboundDebugRendererPacket {

            return ClientboundDebugRendererPacket().apply {
                this.type = type
            }
        }

        fun clear(): ClientboundDebugRendererPacket = base(TYPE_CLEAR)

        fun addCube(
            text: String,
            position: Vector3,
            red: Float,
            green: Float,
            blue: Float,
            alpha: Float,
            durationMillis: Long,
        ): ClientboundDebugRendererPacket {

            return base(TYPE_ADD_CUBE).apply {
                this.text = text
                this.position = position
                this.red = red
                this.green = green
                this.blue = blue
                this.alpha = alpha
                this.durationMillis = durationMillis
            }
        }
    }
}
