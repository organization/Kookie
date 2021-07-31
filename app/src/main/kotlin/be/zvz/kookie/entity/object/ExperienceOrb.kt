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
package be.zvz.kookie.entity.`object`

import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.EntitySizeInfo
import be.zvz.kookie.entity.Human
import be.zvz.kookie.entity.Location
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityIds
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataCollection
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataProperties
import be.zvz.kookie.player.Player
import kotlin.math.pow
import kotlin.math.sqrt

class ExperienceOrb(location: Location, nbt: CompoundTag? = null) : Entity(location, nbt) {
    override val gravity: Float = 0.04F
    override val drag: Float = 0.02F
    override val entityNetworkIdentifier: EntityIds = EntityIds.XP_ORB
    override val initialSizeInfo: EntitySizeInfo = EntitySizeInfo(0.25F, 0.25F)

    var age: Int = 0

    private var lookForTargetTime: Long = 0

    private var targetPlayerRuntimeId: Long? = null

    val targetPlayer: Player?
        get() {
            val player = world.getEntity(targetPlayerRuntimeId ?: -1)
            if (player is Player) {
                return player
            }
            return null
        }

    var xpValue = 0
        set(value) {
            if (value < 0) {
                throw IllegalArgumentException("XP amount range must be greater than 0, got $value")
            }
            field = value
        }

    override fun initEntity(nbt: CompoundTag) {
        super.initEntity(nbt)

        age = nbt.getShort("age", 0)
    }

    override fun saveNBT(): CompoundTag {
        return super.saveNBT().apply {
            setShort("age", age)

            setShort(TAG_VALUE_PC, xpValue)
            setInt(TAG_VALUE_PE, xpValue)
        }
    }

    override fun entityBaseTick(tickDiff: Long): Boolean {
        var hasUpdate = super.entityBaseTick(tickDiff)

        age += tickDiff.toInt()
        if (age > 6000) {
            flagForDespawn()
            return true
        }

        var currentTarget: Human? = targetPlayer

        if (
            currentTarget != null &&
            (!currentTarget.isAlive() || currentTarget.location.distanceSquared(location) > MAX_TARGET_DISTANCE.pow(2))
        ) {
            currentTarget = null
        }
        if (lookForTargetTime >= 20) {
            if (currentTarget == null) {
                val newTarget = world.getNearestEntity(location, MAX_TARGET_DISTANCE.toFloat(), Human::class.java)
                if (newTarget is Human && !(newTarget is Player && false /*TODO: && newTarget.isSpectator()*/)) {
                    currentTarget = newTarget
                }
            }
            lookForTargetTime = 0
        } else {
            lookForTargetTime += tickDiff
        }

        targetPlayerRuntimeId = currentTarget?.getId()

        if (currentTarget != null) {
            val vector = currentTarget.getPosition().add(0.0, (currentTarget.getEyeHeight() / 2).toDouble(), 0.0)
                .subtract(location).divide(MAX_TARGET_DISTANCE)

            val distance = vector.lengthSquared()
            if (distance < 1) {
                setMotion(motion.add(vector.normalize().multiply(0.2 * (1 - sqrt(distance)).pow(2))))
            }

            if (currentTarget.xpManager.canPickUpXp() && boundingBox.intersectsWith(currentTarget.boundingBox)) {
                flagForDespawn()
                currentTarget.xpManager.onPickUpXp(xpValue)
            }
        }
        return hasUpdate
    }

    override fun tryChangeMovement() {
        checkObstruction(location.x.toFloat(), location.y.toFloat(), location.z.toFloat())
        super.tryChangeMovement()
    }

    override fun canBeCollidedWith(): Boolean {
        return false
    }

    override fun syncNetworkData(properties: EntityMetadataCollection) {
        super.syncNetworkData(properties)
        properties.setInt(EntityMetadataProperties.EXPERIENCE_VALUE, xpValue)
    }

    companion object {
        const val TAG_VALUE_PC = "Value" // short
        const val TAG_VALUE_PE = "experience value" // int (WTF?)

        /** Max distance an orb will follow a player across. */
        const val MAX_TARGET_DISTANCE = 8.0

        /** Split sizes used for dropping experience orbs. */
        val ORB_SPLIT_SIZES: List<Int> = listOf(
            2477,
            1237,
            617,
            307,
            149,
            73,
            37,
            17,
            7,
            3,
            1
        ) // This is indexed biggest to smallest so that we can return as soon as we found the biggest value.

        /**
         * Returns the largest size of normal XP orb that will be spawned for the specified amount of XP. Used to split XP
         * up into multiple orbs when an amount of XP is dropped.
         */
        fun getMaxOrbSize(amount: Int): Int {
            ORB_SPLIT_SIZES.forEach {
                if (amount >= it) {
                    return it
                }
            }
            return 1
        }

        fun splitIntoOrbSizes(amount: Int): List<Int> {
            val result = mutableListOf<Int>()
            var amount = amount
            while (amount > 0) {
                val size = getMaxOrbSize(amount)
                result.add(size)
                amount -= size
            }
            return result
        }
    }
}
