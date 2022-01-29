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
package be.zvz.kookie.world.sound

import be.zvz.kookie.entity.Entity
import com.nukkitx.protocol.bedrock.data.SoundEvent

class EntityLongFallSound(
    val entity: Entity
) : StandardSound(SoundEvent.FALL_BIG) {
    override val entityType: String get() = TODO("Implements after implemented Entity::getNetworkTypeId()")
}
