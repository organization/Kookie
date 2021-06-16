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

/**
 * Useless leftover from v1.9 refactor, does nothing
 */
@ProtocolIdentify(ProtocolInfo.IDS.LEVEL_SOUND_EVENT_PACKET_V1)
class LevelSoundEventPacketV1 : DataPacket() {

    var sound: Int = 0
    lateinit var position: Vector3
    var extraData: Int = 0
    var entityType: Int = 1
    var isBabyMob: Boolean = false
    var disableRelativeVolume: Boolean = false

    override fun decodePayload(input: PacketSerializer) {
        sound = input.getByte()
        position = input.getVector3()
        extraData = input.getVarInt()
        entityType = input.getVarInt()
        isBabyMob = input.getBoolean()
        disableRelativeVolume = input.getBoolean()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(sound)
        output.putVector3(position)
        output.putVarInt(extraData)
        output.putVarInt(entityType)
        output.putBoolean(isBabyMob)
        output.putBoolean(disableRelativeVolume)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleLevelSoundEventPacketV1(this)
}
