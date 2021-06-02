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

/**
 * Useless leftover from v1.9 refactor, does nothing
 */
@ProtocolIdentify(ProtocolInfo.IDS.LEVEL_SOUND_EVENT_PACKET_V1)
class LevelSoundEventPacketV1 : DataPacket() {
    override fun decodePayload(input: PacketSerializer) {
        TODO("LevelSoundEventPacketV1 doesn't use")
    }

    override fun encodePayload(output: PacketSerializer) {
        TODO("LevelSoundEventPacketV1 doesn't use")
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleLevelSoundEventPacketV1(this)
    }
}
