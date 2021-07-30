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
package be.zvz.kookie.entity

import be.zvz.kookie.data.bedrock.PotionTypeIdMap
import be.zvz.kookie.data.bedrock.PotionTypeIds
import be.zvz.kookie.entity.`object`.ExperienceOrb
import be.zvz.kookie.entity.`object`.FallingBlock
import be.zvz.kookie.entity.projectile.*
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.StringTag
import be.zvz.kookie.world.World
import com.koloboke.collect.map.hash.HashObjObjMaps

object EntityFactory {
    private val creationFuncs: MutableMap<String, (world: World, nbt: CompoundTag) -> Entity> =
        HashObjObjMaps.newMutableMap()

    private val saveNames: MutableMap<Class<out Entity>, String> = HashObjObjMaps.newMutableMap()

    init {
        // TODO: register default entities on here
        register(Arrow::class.java, { world, nbt ->
            Arrow(EntityDataHelper.parseLocation(nbt, world), null, nbt.getByte(Arrow.TAG_CRITICAL, 0) == 1, nbt)
        }, listOf("Arrow", "minecraft:arrow"))
        register(Egg::class.java, { world, nbt ->
            Egg(EntityDataHelper.parseLocation(nbt, world), null, nbt)
        }, listOf("Egg", "miecraft:egg"))
        register(EnderPearl::class.java, { world, nbt ->
            EnderPearl(EntityDataHelper.parseLocation(nbt, world), null, nbt)
        }, listOf("ThrownEnderpearl", "minecraft:ender_pearl"))
        register(ExperienceBottle::class.java, { world, nbt ->
            ExperienceBottle(EntityDataHelper.parseLocation(nbt, world), null, nbt)
        }, listOf("ThrownXpBottle", "minecraft:xp_bottle"))
        register(ExperienceOrb::class.java, { world, nbt ->
            ExperienceBottle(EntityDataHelper.parseLocation(nbt, world), null, nbt)
        }, listOf("XPOrb", "minecraft:xp_orb"))
        register(FallingBlock::class.java, { world, nbt ->
            FallingBlock(EntityDataHelper.parseLocation(nbt, world), FallingBlock.parseBlockNBT(nbt), nbt)
        }, listOf("FallingBlock", "minecraft:falling_block"))
        // TODO: ItemEntity
        // TODO: Painting
        // TODO: PrimedTNT
        register(Snowball::class.java, { world, nbt ->
            Snowball(EntityDataHelper.parseLocation(nbt, world), null, nbt)
        }, listOf("Snowball", "minecraft:snowball"))
        register(SplashPotion::class.java, { world, nbt ->
            SplashPotion(
                EntityDataHelper.parseLocation(nbt, world),
                null,
                PotionTypeIdMap.fromId(nbt.getShort("PotionId", PotionTypeIds.WATER.id))!!,
                nbt
            )
        }, listOf("ThrownPotion", "minecraft:splash_potion"))
        // TODO: Squid
        // TODO: Villager
        // TODO: Zombie
        register(Human::class.java, { world, nbt ->
            Human(Human.parseSkinNBT(nbt), EntityDataHelper.parseLocation(nbt, world), nbt)
        }, listOf("Human"))
    }

    @JvmStatic
    fun register(
        clazz: Class<out Entity>,
        creationFunc: (world: World, nbt: CompoundTag) -> Entity,
        saveNames: List<String>
    ) {
        if (saveNames.isEmpty()) {
            throw IllegalArgumentException("At least one save name must be provided")
        }
        saveNames.forEach {
            creationFuncs[it] = creationFunc
        }

        this.saveNames[clazz] = saveNames.first()
    }

    @JvmStatic
    fun createFromData(world: World, nbt: CompoundTag): Entity? =
        (nbt.getTag("id") as? StringTag)?.let {
            creationFuncs[it.value]?.invoke(world, nbt)
        }

    @JvmStatic
    fun injectSaveId(clazz: Class<out Entity>, saveData: CompoundTag) {
        if (saveNames.containsKey(clazz)) {
            saveData.setTag("id", StringTag(saveNames.getValue(clazz)))
        } else {
            throw IllegalArgumentException("Entity ${clazz.simpleName} is not registered")
        }
    }

    @JvmStatic
    fun getSaveId(clazz: Class<out Entity>): String = if (saveNames.containsKey(clazz)) {
        saveNames.getValue(clazz)
    } else {
        throw IllegalArgumentException("Entity ${clazz.simpleName} is not registered")
    }
}
