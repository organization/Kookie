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

import be.zvz.kookie.block.Block
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.EntitySizeInfo
import be.zvz.kookie.entity.Location
import be.zvz.kookie.math.RayTraceResult
import be.zvz.kookie.nbt.tag.CompoundTag

abstract class Throwable @JvmOverloads constructor(
    location: Location,
    shootingEntity: Entity?,
    nbt: CompoundTag? = null,
) : Projectile(location, shootingEntity, nbt) {

    override val gravity: Float = 0.03F
    override val drag: Float = 0.01F

    override val initialSizeInfo: EntitySizeInfo = EntitySizeInfo(0.25F, 0.25F)

    override fun onHitBlock(blockHit: Block, hitResult: RayTraceResult) {
        super.onHitBlock(blockHit, hitResult)
        flagForDespawn()
    }
}
