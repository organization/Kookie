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
import be.zvz.kookie.entity.EntitySizeInfo
import be.zvz.kookie.entity.Location
import be.zvz.kookie.event.entity.ProjectileHitEvent
import be.zvz.kookie.event.inventory.InventoryPickupArrowEvent
import be.zvz.kookie.item.VanillaItems
import be.zvz.kookie.math.Math
import be.zvz.kookie.math.RayTraceResult
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityIds
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataCollection
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataFlags
import be.zvz.kookie.player.Player
import kotlin.math.pow
import kotlin.math.sqrt

open class Arrow(
    location: Location,
    shootingEntity: Entity?,
    isCritical: Boolean,
    nbt: CompoundTag? = null,
) : Projectile(location, shootingEntity, nbt) {
    override val gravity: Float = 0.05F
    override val drag: Float = 0.0F
    override val entityNetworkIdentifier: EntityIds = EntityIds.ARROW
    override val initialSizeInfo: EntitySizeInfo = EntitySizeInfo(0.25F, 0.25F)

    override var damage: Float = 2F

    open var pickUpMode: PickupMode = PickupMode.ANY

    open var punchKnockback: Float = 2F

    open var collideTicks: Long = 0

    open var isCritical: Boolean = isCritical
        set(value) {
            field = value
            networkPropertiesDirty = true
        }

    override fun initEntity(nbt: CompoundTag) {
        super.initEntity(nbt)
        pickUpMode = PickupMode.values()[nbt.getByte(TAG_PICKUP)]
        isCritical = nbt.getByte(TAG_CRITICAL, 0) == 1
        collideTicks = nbt.getLong("life", collideTicks)
    }

    override fun saveNBT(): CompoundTag {
        val nbt = super.saveNBT()
        nbt.setByte(TAG_PICKUP, pickUpMode.value)
        nbt.setByte(TAG_CRITICAL, if (isCritical) 1 else 0)
        nbt.setLong("life", collideTicks)
        return nbt
    }

    override fun getResultDamage(): Int {
        val base = super.getResultDamage()
        if (isCritical) {
            return (base + Math.random(0, (base / 2) + 1))
        }
        return base
    }

    override fun entityBaseTick(tickDiff: Long): Boolean {
        if (closed) {
            return false
        }
        var hasUpdate = super.entityBaseTick(tickDiff)
        if (blockHit != null) {
            collideTicks += tickDiff
            if (collideTicks > 1200) {
                flagForDespawn()
                hasUpdate = true
            }
        } else {
            collideTicks = 0
        }
        return hasUpdate
    }

    override fun onHit(event: ProjectileHitEvent) {
        isCritical = false
        // TODO: broadcastSound(ArrowHitSound())
    }

    override fun onHitEntity(entityHit: Entity, hitResult: RayTraceResult) {
        super.onHitEntity(entityHit, hitResult)
        if (punchKnockback > 0) {
            val horizontalSpeed = sqrt(motion.x.pow(2) + motion.z.pow(2))
            if (horizontalSpeed > 0) {
                val multiplier = punchKnockback * 0.6 / horizontalSpeed
                entityHit.setMotion(entityHit.motion.add(motion.x * multiplier, 0.1, motion.z * multiplier))
            }
        }
    }

    override fun onCollideWithPlayer(player: Player) {
        if (blockHit == null) {
            return
        }
        val item = VanillaItems.ARROW

        val playerInventory = player.inventory
        if (/* TODO: player.hasFiniteResources() && */!playerInventory.canAddItem(item.item)) {
            return
        }

        val ev = InventoryPickupArrowEvent(playerInventory, this)
        if (pickUpMode == PickupMode.NONE/* TODO: || (pickUpMode == PickupMode.CREATIVE && !player.isCreative())*/) {
            ev.isCancelled = true
        }

        ev.call()

        if (ev.isAsynchronous) {
            return
        }

        getViewers().forEach { (_, viewer) ->
            // TODO: viewer.networkSession.onPlayerPickItem(player, this)
        }

        playerInventory.addItem(item.item.clone())
        flagForDespawn()
    }

    override fun syncNetworkData(properties: EntityMetadataCollection) {
        super.syncNetworkData(properties)
        properties.setGenericFlag(EntityMetadataFlags.CRITICAL, isCritical)
    }

    enum class PickupMode(val value: Int) {
        NONE(0),
        ANY(1),
        CREATIVE(2)
    }

    companion object {
        private const val TAG_PICKUP = "pickup" // Tag_Byte
        const val TAG_CRITICAL = "crit" // Tag_Byte
    }
}
