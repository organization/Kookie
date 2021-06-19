package be.zvz.kookie.event.entity

import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.Living
import be.zvz.kookie.entity.effect.VanillaEffects
import com.koloboke.collect.map.hash.HashObjObjMaps

open class EntityDamageByEntityEvent(
    private val damagerEntity: Entity,
    entity: Entity,
    cause: Type,
    damage: Float,
    modifiers: MutableMap<EntityDamageEvent.ModifierType, Float> = HashObjObjMaps.newMutableMap(),
    var knockBack: Float
) : EntityDamageEvent(entity, cause, damage, modifiers) {
    val damager: Entity? = damagerEntity
        get() {
            // FIXME: Damager should be long type, and must return entity.world.findEntity(field)
            return if (field?.isClosed() == true) null else field
        }

    init {
        addAttackerModifiers(damagerEntity)
    }

    protected fun addAttackerModifiers(damager: Entity) {
        if (damager is Living) {
            val effects = damager.effectManager
            val strength = effects.get(VanillaEffects.STRENGTH.effect)
            if (strength != null) {
                modifiers[ModifierType.STRENGTH] = baseDamage * 0.3F * strength.effectLevel
            }
            val weekness = effects.get(VanillaEffects.WEAKNESS.effect)
            if (weekness != null) {
                modifiers[ModifierType.WEAKNESS] = -(baseDamage * 0.2F * weekness.effectLevel)
            }
        }
    }
}
