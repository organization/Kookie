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
 * Copyright (C) 2021 - 2022 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.world.sound

import be.zvz.kookie.math.Vector3
import com.nukkitx.math.vector.Vector3f
import com.nukkitx.protocol.bedrock.BedrockPacket
import com.nukkitx.protocol.bedrock.data.LevelEventType
import com.nukkitx.protocol.bedrock.packet.LevelEventPacket

abstract class EventSound(
    val id: LevelEventType,
    open val data: Int = 0
) : Sound {
    override fun encode(pos: Vector3): List<BedrockPacket> =
        listOf(
            LevelEventPacket().apply {
                type = id
                position = Vector3f.ZERO.add(pos.x, pos.y, pos.z)
                data = this@EventSound.data
            }
        )
}
