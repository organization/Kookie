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

@ProtocolIdentify(ProtocolInfo.IDS.CHANGE_DIMENSION_PACKET)
class ChangeDimensionPacket : DataPacket(), ClientboundPacket {
    var dimension: Int = 0
    var position: Vector3 = Vector3()
    var respawn: Boolean = false

    override fun decodePayload(input: PacketSerializer) {
        dimension = input.getVarInt()
        position = input.getVector3()
        respawn = input.getBoolean()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putVarInt(dimension)
        output.putVector3(position)
        output.putBoolean(respawn)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleChangeDimension(this)
    }
}
