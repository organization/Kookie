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

@ProtocolIdentify(ProtocolInfo.IDS.ANVIL_DAMAGE_PACKET)
class AnvilDamagePacket : DataPacket(), ServerboundPacket {
    var damageAmount: Int = 0
    val pos = PacketSerializer.BlockPosition()

    override fun decodePayload(input: PacketSerializer) {
        damageAmount = input.getByte()
        input.getBlockPosition(pos)
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(damageAmount)
        output.putBlockPosition(pos)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleAnvilDamage(this)

    companion object {
        @JvmStatic
        fun create(pos: Vector3, damageAmount: Int): AnvilDamagePacket =
            create(pos.x.toInt(), pos.y.toInt(), pos.z.toInt(), damageAmount)

        @JvmStatic
        fun create(pos: PacketSerializer.BlockPosition, damageAmount: Int): AnvilDamagePacket =
            create(pos.x, pos.y, pos.z, damageAmount)

        @JvmStatic
        fun create(x: Int, y: Int, z: Int, damageAmount: Int): AnvilDamagePacket = AnvilDamagePacket().apply {
            this.pos.x = x
            this.pos.y = y
            this.pos.z = z
            this.damageAmount = damageAmount
        }
    }
}
