package be.zvz.kookie.entity.effect

import be.zvz.kookie.color.Color
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.Living

open class Effect(
    val internalRuntimeId: Int,
    val name: String,
    val color: Color,
    val isBad: Boolean = false,
    val hasBubbles: Boolean = true
) {

    open val defaultDuration: Int = 600

    open fun canTick(instance: EffectInstance): Boolean = false

    open fun applyEffect(entity: Living, instance: EffectInstance, potency: Float = 1.0f, source: Entity? = null) {
    }

    open fun add(entity: Living, instance: EffectInstance) {
    }

    open fun remove(entity: Living, instance: EffectInstance) {
    }
}
