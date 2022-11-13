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
 * Copyright (C) 2021 - 2022 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.network.mcpe.convert

import be.zvz.kookie.block.VanillaBlocks
import be.zvz.kookie.item.Durable
import be.zvz.kookie.item.Item
import be.zvz.kookie.item.ItemFactory
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.IntTag
import be.zvz.kookie.network.mcpe.protocol.types.inventory.ItemStack
import be.zvz.kookie.network.mcpe.protocol.types.recipe.RecipeIngredient
import be.zvz.kookie.player.GameMode
import be.zvz.kookie.network.mcpe.protocol.types.GameMode as ProtocolGameMode

object TypeConverter {

    private const val DAMAGE_TAG = "Damage" // TAG_Int
    private const val DAMAGE_TAG_CONFLICT_RESOLUTION = "___Damage_ProtocolCollisionResolution___"
    private const val PM_META_TAG = "___Meta___"

    private val shieldRuntimeId: Int =
        GlobalItemTypeDictionary.dictionary.fromStringId("minecraft:shield")

    @JvmStatic
    fun coreGameModeToProtocol(gameMode: GameMode): Int = when (gameMode) {
        GameMode.SURVIVAL -> ProtocolGameMode.SURVIVAL
        GameMode.CREATIVE, GameMode.SPECTATOR -> ProtocolGameMode.CREATIVE
        GameMode.ADVENTURE -> ProtocolGameMode.ADVENTURE
    }

    @JvmStatic
    fun protocolGameModeName(gameMode: GameMode): String = when (gameMode) {
        GameMode.SURVIVAL -> "Survival"
        GameMode.ADVENTURE -> "Adventure"
        else -> "Creative"
    }

    @JvmStatic
    fun protocolGameModeToCore(gameMode: Int): GameMode? = when (gameMode) {
        ProtocolGameMode.SURVIVAL -> GameMode.SURVIVAL
        ProtocolGameMode.CREATIVE -> GameMode.CREATIVE
        ProtocolGameMode.ADVENTURE -> GameMode.ADVENTURE
        ProtocolGameMode.SURVIVAL_VIEWER, ProtocolGameMode.CREATIVE_VIEWER -> GameMode.SPECTATOR
        else -> null
    }

    @JvmStatic
    fun coreItemStackToRecipeIngredient(itemStack: Item): RecipeIngredient {
        if (itemStack.isNull()) {
            return RecipeIngredient(0, 0, 0)
        }
        val id: Int
        val meta: Int
        if (itemStack.hasAnyDamageValue()) {
            TODO()
        }
        return RecipeIngredient(0, 0, 0)
    }

    @JvmStatic
    fun coreItemStackToNet(item: Item): ItemStack {
        if (item.isNull()) {
            return ItemStack.empty()
        }
        var nbt: CompoundTag? = null
        if (item.hasNamedTag()) {
            nbt = item.getNamedTag().clone() as CompoundTag
        }
        val isBlockItem = item.getId() < 256
        if (item is Durable && item.damage > 0) {
            nbt = nbt?.apply {
                val existing = getTag(DAMAGE_TAG)
                if (existing != null) {
                    removeTag(DAMAGE_TAG)
                    setTag(DAMAGE_TAG_CONFLICT_RESOLUTION, existing)
                }
            } ?: CompoundTag.create()
            nbt.setInt(DAMAGE_TAG, item.damage)
        } else if (isBlockItem && item.getMeta() != 0) {
            if (nbt == null) {
                nbt = CompoundTag.create()
            }
            nbt.setInt(PM_META_TAG, item.getMeta())
        }
        val (id, meta) = ItemTranslator.toNetworkId(item.getId(), item.getMeta())
        var blockRuntimeId = 0
        if (isBlockItem) {
            val block = item.getBlock()
            if (block.getId() != VanillaBlocks.AIR.id) {
                // TODO: blockRuntimeId = RuntimeBlockMapping.toRuntimeId(block.getFullId())
            }
        }
        return ItemStack(
            id,
            meta,
            item.count,
            blockRuntimeId,
            nbt,
            listOf(),
            listOf(),
            if (id == shieldRuntimeId) 0 else null
        )
    }

    @JvmStatic
    fun netItemStackToCore(itemStack: ItemStack): Item {
        if (itemStack.id == 0) {
            return ItemFactory.air()
        }
        var compound = itemStack.nbt

        var (id, meta) = ItemTranslator.fromNetworkId(itemStack.id, itemStack.meta)

        if (compound != null) {
            compound = compound.clone() as CompoundTag
            val damageTag = compound.getTag(DAMAGE_TAG)
            val metaTag = compound.getTag(PM_META_TAG)
            if (damageTag is IntTag) {
                meta = damageTag.value
                compound.removeTag(DAMAGE_TAG)
                val conflicted = compound.getTag(DAMAGE_TAG_CONFLICT_RESOLUTION)
                if (conflicted != null) {
                    compound.removeTag(DAMAGE_TAG_CONFLICT_RESOLUTION)
                    compound.setTag(DAMAGE_TAG, conflicted)
                } else if (compound.count() == 0) {
                    compound = null
                }
            } else if (metaTag is IntTag) {
                meta = metaTag.value
                compound.removeTag(PM_META_TAG)
                if (compound.count() == 0) {
                    compound = null
                }
            }
        }
        return ItemFactory.get(
            id,
            meta,
            itemStack.count,
            compound
        )
    }
}
