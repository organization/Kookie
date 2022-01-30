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
package be.zvz.kookie.world.particle

import com.nukkitx.protocol.bedrock.data.LevelEventType

abstract class StandardParticle @JvmOverloads constructor(
    type: LevelEventType,
    data: Int = 0
) : EventParticle(type, data) {
    enum class Type(val id: Int) {
        BUBBLE(1),
        BUBBLE_MANUAL(2),
        CRITICAL(3),
        BLOCK_FORCE_FIELD(4),
        SMOKE(5),
        EXPLODE(6),
        EVAPORATION(7),
        FLAME(8),
        LAVA(9),
        LARGE_SMOKE(10),
        REDSTONE(11),
        RISING_RED_DUST(12),
        ITEM_BREAK(13),
        SNOWBALL_POOF(14),
        HUGE_EXPLODE(15),
        HUGE_EXPLODE_SEED(16),
        MOB_FLAME(17),
        HEART(18),
        TERRAIN(19),
        SUSPENDED_TOWN(20),
        PORTAL(21),
        WATER_SPLASH(23),
        WATER_SPLASH_MANUAL(24),
        WATER_WAKE(25),
        DRIP_WATER(26),
        DRIP_LAVA(27),
        DRIP_HONEY(28),
        STALACTITE_DRIP_WATER(29),
        STALACTITE_DRIP_LAVA(30),
        DUST(31),
        MOB_SPELL(32),
        MOB_SPELL_AMBIENT(33),
        MOB_SPELL_INSTANTANEOUS(34),
        INK(35),
        SLIME(36),
        RAIN_SPLASH(37),
        VILLAGER_ANGRY(38),
        VILLAGER_HAPPY(39),
        ENCHANTMENT_TABLE(40),
        TRACKING_EMITTER(41),
        NOTE(42),
        WITCH_SPELL(43),
        CARROT(44),
        MOB_APPEARANCE(45),
        END_ROD(46),
        DRAGONS_BREATH(47),
        SPIT(48),
        TOTEM(49),
        FOOD(50),
        FIREWORKS_STARTER(51),
        FIREWORKS_SPARK(52),
        FIREWORKS_OVERLAY(53),
        BALLOON_GAS(54),
        COLORED_FLAME(55),
        SPARKLER(56),
        CONDUIT(57),
        BUBBLE_COLUMN_UP(58),
        BUBBLE_COLUMN_DOWN(59),
        SNEEZE(60),
        SHULKER_BULLET(61),
        BLEACH(62),
        DRAGON_DESTROY_BLOCK(63),
        MYCELIUM_DUST(64),
        FALLING_RED_DUST(65),
        CAMPFIRE_SMOKE(66),
        TALL_CAMPFIRE_SMOKE(67),
        DRAGON_BREATH_FIRE(68),
        DRAGON_BREATH_TRAIL(69),
        BLUE_FLAME(70),
        SOUL(71),
        OBSIDIAN_TEAR(72),
        PORTAL_REVERSE(73),
        SNOWFLAKE(74),
        VIBRATION_SIGNAL(75),
        SCULK_SENSOR_REDSTONE(76),
        SPORE_BLOSSOM_SHOWER(77),
        SPORE_BLOSSOM_AMBIENT(78),
        WAX(79),
        ELECTRIC_SPARK(80);

        companion object {
            private val VALUES = values()

            @JvmStatic fun from(findValue: Int) = VALUES.first { it.id == findValue }
        }
    }
}
