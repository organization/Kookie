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
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer
import java.util.concurrent.atomic.AtomicInteger

@ProtocolIdentify(ProtocolInfo.IDS.ANVIL_DAMAGE_PACKET)
class AnvilDamagePacket : DataPacket(), ServerboundPacket {
    var x: Int = 0
    var y: Int = 0
    var z: Int = 0
    var damageAmount: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        damageAmount = input.getByte()
        val x = AtomicInteger()
        val y = AtomicInteger()
        val z = AtomicInteger()
        input.getBlockPosition(x, y, z)

        this.x = x.get()
        this.y = y.get()
        this.z = z.get()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(damageAmount)
        output.putBlockPosition(x, y, z)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleAnvilDamage(this)
    }

    companion object {
        fun create(x: Int, y: Int, z: Int, damageAmount: Int): AnvilDamagePacket {
            val packet = AnvilDamagePacket().apply {
                this.x = x
                this.y = y
                this.z = z
                this.damageAmount = damageAmount
            }

            return packet
        }
    }
}
