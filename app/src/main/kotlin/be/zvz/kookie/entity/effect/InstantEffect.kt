package be.zvz.kookie.entity.effect

import be.zvz.kookie.color.Color

open class InstantEffect(
    internalRuntimeId: Int,
    name: String,
    color: Color,
    bad: Boolean = false,
    hasBubbles: Boolean = true
) : Effect(
    internalRuntimeId,
    name,
    color,
    bad,
    hasBubbles
) {
    override val defaultDuration: Int = 1

    override fun canTick(instance: EffectInstance): Boolean = true
}
