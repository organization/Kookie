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
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.entity.Living

class PoisonEffect(
    internalRuntimeId: Int,
    name: String,
    color: Color,
    bad: Boolean = false,
    hasBubbles: Boolean = true,
    val fatal: Boolean = false
) : Effect(internalRuntimeId, name, color, bad, hasBubbles) {

    override fun canTick(instance: EffectInstance): Boolean {
        /*
        if(($interval = (25 >> $instance->getAmplifier())) > 0){
			return ($instance->getDuration() % $interval) === 0;
		}
         */
        val interval = 25 shr instance.amplifier
        if (interval > 0) {
            return instance.duration % interval == 0
        }
        return true
    }

    override fun applyEffect(entity: Living, instance: EffectInstance, potency: Float, source: Entity?) {
        /*
        TODO:
        if($entity->getHealth() > 1 or $this->fatal){
			$ev = new EntityDamageEvent($entity, EntityDamageEvent::CAUSE_MAGIC, 1);
			$entity->attack($ev);
		}
         */
    }
}
