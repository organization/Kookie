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

@ProtocolIdentify(ProtocolInfo.IDS.MOB_EFFECT_PACKET)
class MobEffectPacket : DataPacket(), ClientboundPacket {

    var entityRuntimeId: Long = 0
    var eventId: Int = 0
    var effectId: Int = 0
    var amplifier: Int = 0
    var particles: Boolean = true
    var duration: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        entityRuntimeId = input.getEntityRuntimeId()
        eventId = input.getByte()
        effectId = input.getVarInt()
        amplifier = input.getVarInt()
        particles = input.getBoolean()
        duration = input.getVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityRuntimeId(entityRuntimeId)
        output.putByte(eventId)
        output.putVarInt(effectId)
        output.putVarInt(amplifier)
        output.putBoolean(particles)
        output.putVarInt(duration)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleMobEffect(this)

    companion object {
        const val EVENT_ADD = 1
        const val EVENT_MODIFY = 2
        const val EVENT_REMOVE = 3
    }
}
