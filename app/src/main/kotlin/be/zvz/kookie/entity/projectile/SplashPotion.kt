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

import be.zvz.kookie.block.VanillaBlocks
import be.zvz.kookie.color.Color
import be.zvz.kookie.data.bedrock.PotionTypeIdMap
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.Living
import be.zvz.kookie.entity.Location
import be.zvz.kookie.entity.effect.InstantEffect
import be.zvz.kookie.event.entity.ProjectileHitBlockEvent
import be.zvz.kookie.event.entity.ProjectileHitEntityEvent
import be.zvz.kookie.event.entity.ProjectileHitEvent
import be.zvz.kookie.item.PotionType
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityIds
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataCollection
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataFlags
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataProperties
import be.zvz.kookie.world.particle.Particle
import be.zvz.kookie.world.particle.PotionSplashParticle
import be.zvz.kookie.world.sound.PotionSplashSound
import kotlin.math.sqrt

class SplashPotion(
    location: Location,
    shootingEntity: Entity?,
    val type: PotionType,
    nbt: CompoundTag?
) : Throwable(
    location,
    shootingEntity,
    nbt
) {

    override val entityNetworkIdentifier: EntityIds = EntityIds.SPLASH_POTION

    override val gravity: Float = 0.05F
    override val drag: Float = 0.1F

    private val linger: Boolean = false

    override fun saveNBT(): CompoundTag {
        return super.saveNBT().apply {
            setShort("PotionId", PotionTypeIdMap.toId(type)!!)
        }
    }

    override fun getResultDamage(): Int {
        return -1
    }

    override fun onHit(event: ProjectileHitEvent) {
        val effects = type.effectsGetter()
        var hasEffects = true

        val particle: Particle

        if (effects.isEmpty()) {
            particle = PotionSplashParticle(Color(0x38, 0x5d, 0xc6))
            hasEffects = false
        } else {
            val colors: MutableList<Color> = mutableListOf()
            effects.forEach {
                val level = it.effectLevel
                for (j in 0 until level) {
                    colors.add(it.color)
                }
            }
            particle = PotionSplashParticle(Color.mix(*colors.toTypedArray()))
        }

        world.addParticle(location, particle)
        broadcastSound(PotionSplashSound())

        if (hasEffects) {
            if (!linger) {
                world.getNearbyEntities(boundingBox.expandedCopy(4.125, 2.125, 4.125)).forEach { entity ->
                    if (entity is Living && entity.isAlive()) {
                        val distanceSquared = entity.getEyePos().distanceSquared(location)
                        if (distanceSquared > 16) {
                            return@forEach
                        }

                        var distanceMultiplier = 1 - (sqrt(distanceSquared) / 4)

                        if (event is ProjectileHitEntityEvent && entity == event.entityHit) {
                            distanceMultiplier = 1.0
                        }
                        type.effectsGetter().forEach forEach2@{ instance ->
                            if (instance.effectType !is InstantEffect) {
                                val newDuration = instance.duration * 0.75 * distanceMultiplier
                                if (newDuration < 20) {
                                    return@forEach2
                                }
                                instance.duration = newDuration.toInt()
                                entity.effectManager.add(instance)
                            } else {
                                instance.effectType.applyEffect(entity, instance, distanceMultiplier.toFloat(), this)
                            }
                        }
                    }
                }
            }
        } else if (event is ProjectileHitBlockEvent && type == PotionType.WATER) {
            val blockIn = event.blockHit.getSide(event.rayTraceResult.hitFace)

            if (blockIn.getId() == VanillaBlocks.FIRE.id) {
                world.setBlock(blockIn.pos, VanillaBlocks.AIR.block)
            }
            blockIn.getHorizontalSides().forEach {
                if (it.getId() == VanillaBlocks.FIRE.id) {
                    world.setBlock(it.pos, VanillaBlocks.AIR.block)
                }
            }
        }
    }

    override fun syncNetworkData(properties: EntityMetadataCollection) {
        super.syncNetworkData(properties)
        properties.setShort(EntityMetadataProperties.POTION_AUX_VALUE, PotionTypeIdMap.toId(type)!!)
        properties.setGenericFlag(EntityMetadataFlags.LINGER, linger)
    }
}