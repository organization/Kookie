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
package be.zvz.kookie.event.entity

import be.zvz.kookie.entity.Entity
import be.zvz.kookie.event.Cancellable
import com.koloboke.collect.map.hash.HashObjObjMaps
import kotlin.math.max

open class EntityDamageEvent @JvmOverloads constructor(
    entity: Entity,
    var cause: Type,
    var damage: Float,
    val modifiers: MutableMap<ModifierType, Float> = HashObjObjMaps.newMutableMap()
) : EntityEvent(entity), Cancellable {

    @JvmOverloads
    constructor(
        entity: Entity,
        cause: Type,
        damage: Int,
        modifiers: MutableMap<ModifierType, Float> = HashObjObjMaps.newMutableMap()
    ) : this(entity, cause, damage.toFloat(), modifiers)

    override var isCancelled: Boolean = false

    var baseDamage: Float = damage
    val originalBase: Float = damage
    val originals: MutableMap<ModifierType, Float> = modifiers

    var attackCooldown: Int = 0

    fun getOriginalBaseDamage(): Float = originalBase

    fun getOriginalModifier(type: ModifierType): Float = originals[type] ?: 0F

    fun isApplicable(type: ModifierType): Boolean = modifiers.containsKey(type)

    fun getFinalDamage(): Float = max(0F, baseDamage + modifiers.values.sum())

    fun canBeReducedByArmor(): Boolean = when (cause) {
        Type.FIRE_TICK, Type.SUFFOCATION, Type.DROWNING, Type.STARVATION, Type.FALL, Type.VOID, Type.MAGIC, Type.SUICIDE -> false
        else -> true
    }

    enum class Type(cause: Int) {
        CONTACT(0),
        ENTITY_ATTACK(1),
        PROJECTILE(2),
        SUFFOCATION(3),
        FALL(4),
        FIRE(5),
        FIRE_TICK(6),
        LAVA(7),
        DROWNING(8),
        BLOCK_EXPLOSION(9),
        ENTITY_EXPLOSION(10),
        VOID(11),
        SUICIDE(12),
        MAGIC(13),
        CUSTOM(14),
        STARVATION(15)
    }

    enum class ModifierType(type: Int) {
        ARMOR(1),
        STRENGTH(2),
        WEAKNESS(3),
        RESISTANCE(4),
        ABSORPTION(5),
        ARMOR_ENCHANTMENTS(6),
        CRITICAL(7),
        TOTEM(8),
        WEAPON_ENCHANTMENTS(9),
        PREVIOUS_DAMAGE_COOLDOWN(10)
    }
}
