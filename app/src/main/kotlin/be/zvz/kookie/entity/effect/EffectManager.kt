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
package be.zvz.kookie.entity.effect

import be.zvz.kookie.color.Color
import be.zvz.kookie.entity.Living
import com.koloboke.collect.map.hash.HashIntObjMaps
import kotlin.math.abs

data class EffectManager(val entity: Living) {

    protected var effects: MutableMap<Int, EffectInstance> = HashIntObjMaps.newMutableMap()

    var bubbleColor: Color = Color(0, 0, 0, 0)
        private set

    var onlyAmbientEffects: Boolean = false
        private set

    var effectAddHooks: MutableList<(instance: EffectInstance, replaceOldEffect: Boolean) -> Unit> = mutableListOf()

    var effectRemoveHooks: MutableList<(instance: EffectInstance) -> Unit> = mutableListOf()

    fun all(): MutableMap<Int, EffectInstance> = effects

    fun clear() {
        effects.apply {
            val iterator = iterator()
            while (iterator.hasNext()) {
                this@EffectManager.remove(iterator.next().value.effectType, false)
                iterator.remove()
            }
        }
    }

    fun remove(type: Effect, removeElement: Boolean = true) {
        val index = type.internalRuntimeId
        if (effects.containsKey(index)) {
            val effect = effects.getValue(index)
            val hasExpired = effect.hasExpired()
            /*
            TODO:
            ev = EnttiyEffectRemoveEvent(entity, effect)
            ev.call()
            if (ev.isCancelled()) {
            if (hasExpired && !ev.getEffect().hasExpired()) {
            effectAddHooks.forEach {
            (it)(ev.getEffect(), true)
            }
            }
            return
            }
             */
            if (removeElement) {
                effects.remove(index)
            }
            effect.effectType.remove(entity, effect)
            effectRemoveHooks.forEach {
                (it)(effect)
            }

            recalculateEffectColor()
        }
    }

    fun get(effect: Effect): EffectInstance? = effects[effect.internalRuntimeId]

    fun has(effect: Effect): Boolean = get(effect) != null

    fun add(effect: EffectInstance): Boolean {
        var oldEffect: EffectInstance? = null
        var cancelled = false

        val index = effect.effectType.internalRuntimeId
        if (effects.containsKey(index)) {
            oldEffect = effects.getValue(index)
            if (
                abs(effect.amplifier) < oldEffect.amplifier ||
                (abs(effect.amplifier) == abs(oldEffect.amplifier)) && effect.duration < oldEffect.duration
            ) {
                cancelled = true
            }
        }
        /*
        val ev = EntityEffectAddEvent(entity, effect, oldEffect)
        if (cancelled) {
        event.cancel()
        }
        ev.call()

        if (ev.isCancelled()) {
        return false
        }
         */
        oldEffect?.effectType?.remove(entity, oldEffect)

        effect.effectType.add(entity, effect)

        effectAddHooks.forEach {
            (it)(effect, oldEffect != null)
        }
        effects[index] = effect
        recalculateEffectColor()
        return true
    }

    fun tick(tickDiff: Long): Boolean {
        effects.forEach { (_, effectInstance) ->
            val type = effectInstance.effectType
            if (type.canTick(effectInstance)) {
                type.applyEffect(entity, effectInstance)
            }
            effectInstance.decreaseDuration(tickDiff.toInt())
            if (effectInstance.hasExpired()) {
                remove(effectInstance.effectType)
            }
        }
        return effects.isNotEmpty()
    }

    fun recalculateEffectColor() {
        val colors: MutableList<Color> = mutableListOf()
        var ambient = true

        effects.forEach { (_, effect) ->
            if (effect.visible && effect.effectType.hasBubbles) {
                val level = effect.effectLevel
                val color = effect.color
                repeat(level) {
                    colors.add(color)
                }
                if (!effect.ambient) {
                    ambient = false
                }
            }
        }
        if (colors.size > 0) {
            bubbleColor = Color.mix(colors.removeFirst(), *colors.toTypedArray())
            onlyAmbientEffects = ambient
        } else {
            bubbleColor = Color(0, 0, 0, 0)
            onlyAmbientEffects = false
        }
    }
}
