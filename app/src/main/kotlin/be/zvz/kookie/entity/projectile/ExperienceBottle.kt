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
package be.zvz.kookie.entity.projectile

import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.Location
import be.zvz.kookie.event.entity.ProjectileHitEvent
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityIds

open class ExperienceBottle @JvmOverloads constructor(
    location: Location,
    shootingEntity: Entity?,
    nbt: CompoundTag? = null,
) : Throwable(
    location, shootingEntity,
    nbt
) {
    override val entityNetworkIdentifier: EntityIds = EntityIds.XP_BOTTLE

    override val gravity: Float = 0.07F

    override fun getResultDamage(): Int {
        return -1
    }

    override fun onHit(event: ProjectileHitEvent) {
        /*
        TODO: 
        world.addParticle(location, PotionSplashParticle(PotionSplashParticle.DEFAULT_COLOR))
        broadcastSound(PotionSplashSound())
        world.dropExperience(location, Math.random(3, 11))
        
         */
    }
}
