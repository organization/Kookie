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
import be.zvz.kookie.event.entity.EntityDamageEvent
import be.zvz.kookie.event.entity.ProjectileHitEvent
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityIds

class EnderPearl(
    location: Location,
    shootingEntity: Entity?,
    nbt: CompoundTag?,
) : Throwable(
    location, shootingEntity,
    nbt
) {
    override val entityNetworkIdentifier: EntityIds = EntityIds.ENDER_PEARL

    override fun onHit(event: ProjectileHitEvent) {
        owningEntity?.let { owner ->
            val origin = owner.getPosition()
            // TODO: world.addParticle(origin, EndermanTeleportParticle())
            // TODO: world.addSound(origin, EndermanTeleportSound())
            val target = event.rayTraceResult.hitVector
            owner.teleport(target)
            // TODO: world.addSound(target, EndermanTeleportSound())

            owner.attack(EntityDamageEvent(owner, EntityDamageEvent.Type.FALL, 5F))
        }
    }
}
