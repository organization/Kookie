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
package be.zvz.kookie.item

import be.zvz.kookie.block.BlockFactory
import be.zvz.kookie.inventory.ArmorInventory
import be.zvz.kookie.nbt.tag.CompoundTag
import com.koloboke.collect.map.hash.HashIntObjMaps

object ItemFactory {
    val list: MutableMap<Int, Item> = HashIntObjMaps.newMutableMap()

    init {
        registerArmorItems()
        registerSpawnEggs()
        registerTierToolItems()

        register(Apple(ItemIdentifier(ItemIds.APPLE.id, 0), "Apple"))
        register(Arrow(ItemIdentifier(ItemIds.ARROW.id, 0), "Arrow"))

        TODO("Add Items")
    }

    private fun registerArmorItems() {
        val registerArmor = { id: Int, name: String, defencePoints: Int, maxDurability: Int, slot: Int ->
            register(Armor(ItemIdentifier(id, 0), name, ArmorTypeInfo(defencePoints, maxDurability, slot)))
        }

        registerArmor(ItemIds.CHAIN_BOOTS.id, "Chainmail Boots", 1, 196, ArmorInventory.SLOT_FEET)
        registerArmor(ItemIds.DIAMOND_BOOTS.id, "Diamond Boots", 3, 430, ArmorInventory.SLOT_FEET)
        registerArmor(ItemIds.GOLDEN_BOOTS.id, "Golden Boots", 1, 92, ArmorInventory.SLOT_FEET)
        registerArmor(ItemIds.IRON_BOOTS.id, "Iron Boots", 2, 196, ArmorInventory.SLOT_FEET)
        registerArmor(ItemIds.LEATHER_BOOTS.id, "Leather Boots", 1, 66, ArmorInventory.SLOT_FEET)
        registerArmor(ItemIds.NETHERITE_BOOTS.id, "Netherite Boots", 3, 481, ArmorInventory.SLOT_FEET)

        registerArmor(ItemIds.CHAIN_CHESTPLATE.id, "Chainmail Chestplate", 5, 241, ArmorInventory.SLOT_CHEST)
        registerArmor(ItemIds.DIAMOND_CHESTPLATE.id, "Diamond Chestplate", 8, 529, ArmorInventory.SLOT_CHEST)
        registerArmor(ItemIds.GOLDEN_CHESTPLATE.id, "Golden Chestplate", 5, 113, ArmorInventory.SLOT_CHEST)
        registerArmor(ItemIds.IRON_CHESTPLATE.id, "Iron Chestplate", 6, 241, ArmorInventory.SLOT_CHEST)
        registerArmor(ItemIds.LEATHER_CHESTPLATE.id, "Leather Tunic", 3, 81, ArmorInventory.SLOT_CHEST)
        registerArmor(ItemIds.NETHERITE_CHESTPLATE.id, "Netherite Chestplate", 8, 592, ArmorInventory.SLOT_CHEST)

        registerArmor(ItemIds.CHAIN_HELMET.id, "Chainmail Helmet", 2, 166, ArmorInventory.SLOT_HEAD)
        registerArmor(ItemIds.DIAMOND_HELMET.id, "Diamond Helmet", 3, 364, ArmorInventory.SLOT_HEAD)
        registerArmor(ItemIds.GOLDEN_HELMET.id, "Golden Helmet", 2, 78, ArmorInventory.SLOT_HEAD)
        registerArmor(ItemIds.IRON_HELMET.id, "Iron Helmet", 2, 166, ArmorInventory.SLOT_HEAD)
        registerArmor(ItemIds.LEATHER_HELMET.id, "Leather Cap", 1, 56, ArmorInventory.SLOT_HEAD)
        registerArmor(ItemIds.NETHERITE_HELMET.id, "Netherite Helmet", 3, 407, ArmorInventory.SLOT_HEAD)

        registerArmor(ItemIds.CHAIN_LEGGINGS.id, "Chainmail Leggings", 4, 226, ArmorInventory.SLOT_LEGS)
        registerArmor(ItemIds.DIAMOND_LEGGINGS.id, "Diamond Leggings", 6, 496, ArmorInventory.SLOT_LEGS)
        registerArmor(ItemIds.GOLDEN_LEGGINGS.id, "Golden Leggings", 3, 106, ArmorInventory.SLOT_LEGS)
        registerArmor(ItemIds.IRON_LEGGINGS.id, "Iron Leggings", 5, 226, ArmorInventory.SLOT_LEGS)
        registerArmor(ItemIds.LEATHER_LEGGINGS.id, "Leather Pants", 2, 76, ArmorInventory.SLOT_LEGS)
        registerArmor(ItemIds.NETHERITE_LEGGINGS.id, "Netherite Leggings", 6, 555, ArmorInventory.SLOT_LEGS)
    }

    private fun registerSpawnEggs() {
        // TODO: the meta values should probably be hardcoded; they won't change, but the EntityLegacyIds might
    }

    private fun registerTierToolItems() {
        register(Axe(ItemIdentifier(ItemIds.NETHERITE_AXE.id, 0), "Netherite Axe", ToolTier.NETHERITE))
        register(Axe(ItemIdentifier(ItemIds.DIAMOND_AXE.id, 0), "Diamond Axe", ToolTier.DIAMOND))
        register(Axe(ItemIdentifier(ItemIds.GOLDEN_AXE.id, 0), "Golden Axe", ToolTier.GOLD))
        register(Axe(ItemIdentifier(ItemIds.IRON_AXE.id, 0), "Iron Axe", ToolTier.IRON))
        register(Axe(ItemIdentifier(ItemIds.STONE_AXE.id, 0), "Stone Axe", ToolTier.STONE))
        register(Axe(ItemIdentifier(ItemIds.WOODEN_AXE.id, 0), "Wooden Axe", ToolTier.WOOD))

        register(Hoe(ItemIdentifier(ItemIds.NETHERITE_HOE.id, 0), "Netherite Hoe", ToolTier.NETHERITE))
        register(Hoe(ItemIdentifier(ItemIds.DIAMOND_HOE.id, 0), "Diamond Hoe", ToolTier.DIAMOND))
        register(Hoe(ItemIdentifier(ItemIds.GOLDEN_HOE.id, 0), "Golden Hoe", ToolTier.GOLD))
        register(Hoe(ItemIdentifier(ItemIds.IRON_HOE.id, 0), "Iron Hoe", ToolTier.IRON))
        register(Hoe(ItemIdentifier(ItemIds.STONE_HOE.id, 0), "Stone Hoe", ToolTier.STONE))
        register(Hoe(ItemIdentifier(ItemIds.WOODEN_HOE.id, 0), "Wooden Hoe", ToolTier.WOOD))

        register(Pickaxe(ItemIdentifier(ItemIds.NETHERITE_PICKAXE.id, 0), "Netherite Pickaxe", ToolTier.NETHERITE))
        register(Pickaxe(ItemIdentifier(ItemIds.DIAMOND_PICKAXE.id, 0), "Diamond Pickaxe", ToolTier.DIAMOND))
        register(Pickaxe(ItemIdentifier(ItemIds.GOLDEN_PICKAXE.id, 0), "Golden Pickaxe", ToolTier.GOLD))
        register(Pickaxe(ItemIdentifier(ItemIds.IRON_PICKAXE.id, 0), "Iron Pickaxe", ToolTier.IRON))
        register(Pickaxe(ItemIdentifier(ItemIds.STONE_PICKAXE.id, 0), "Stone Pickaxe", ToolTier.STONE))
        register(Pickaxe(ItemIdentifier(ItemIds.WOODEN_PICKAXE.id, 0), "Wooden Pickaxe", ToolTier.WOOD))

        register(Shovel(ItemIdentifier(ItemIds.NETHERITE_SHOVEL.id, 0), "Netherite Shovel", ToolTier.NETHERITE))
        register(Shovel(ItemIdentifier(ItemIds.DIAMOND_SHOVEL.id, 0), "Diamond Shovel", ToolTier.DIAMOND))
        register(Shovel(ItemIdentifier(ItemIds.GOLDEN_SHOVEL.id, 0), "Golden Shovel", ToolTier.GOLD))
        register(Shovel(ItemIdentifier(ItemIds.IRON_SHOVEL.id, 0), "Iron Shovel", ToolTier.IRON))
        register(Shovel(ItemIdentifier(ItemIds.STONE_SHOVEL.id, 0), "Stone Shovel", ToolTier.STONE))
        register(Shovel(ItemIdentifier(ItemIds.WOODEN_SHOVEL.id, 0), "Wooden Shovel", ToolTier.WOOD))

        register(Sword(ItemIdentifier(ItemIds.NETHERITE_SWORD.id, 0), "Netherite Sword", ToolTier.NETHERITE))
        register(Sword(ItemIdentifier(ItemIds.DIAMOND_SWORD.id, 0), "Diamond Sword", ToolTier.DIAMOND))
        register(Sword(ItemIdentifier(ItemIds.GOLDEN_SWORD.id, 0), "Golden Sword", ToolTier.GOLD))
        register(Sword(ItemIdentifier(ItemIds.IRON_SWORD.id, 0), "Iron Sword", ToolTier.IRON))
        register(Sword(ItemIdentifier(ItemIds.STONE_SWORD.id, 0), "Stone Sword", ToolTier.STONE))
        register(Sword(ItemIdentifier(ItemIds.WOODEN_SWORD.id, 0), "Wooden Sword", ToolTier.WOOD))
    }

    fun register(item: Item, override: Boolean = false) {
        val id: Int = item.getId()
        val variant = item.getMeta()

        if (!override && isRegistered(id, variant)) {
            throw RuntimeException("Trying to overwrite an already registered item")
        }
        list[getListOffset(id, variant)] = item.clone()
    }

    fun remap(identifier: ItemIdentifier, item: Item, override: Boolean = false) {
        if (!override && isRegistered(identifier.id, identifier.meta)) {
            throw RuntimeException("Trying to overwrite an already registered item")
        }

        list[getListOffset(identifier.id, identifier.meta)] = item.clone()
    }

    fun get(id: Int, meta: Int = 0, count: Int = 1, tags: CompoundTag? = null): Item {
        val offset = getListOffset(id, meta)
        val zero = getListOffset(id, 0)

        var item: Item? = when {
            meta != -1 -> when {
                list.containsKey(offset) -> list[offset]
                list.containsKey(zero) -> {
                    val durableItem = list[zero]
                    if (durableItem is Durable && meta <= durableItem.maxDurability) {
                        val clone = durableItem.clone() as Durable
                        clone.damage = meta
                        clone
                    } else
                        null
                }
                id < 256 -> ItemBlock(ItemIdentifier(id, meta), BlockFactory.get(if (id < 0) -id else id, meta and 0xf))
                else -> null
            }
            else -> null
        }

        if (item === null) {
            item = Item(ItemIdentifier(id, meta))
        }

        item.count = count
        if (tags != null) {
            item.setNamedTag(tags)
        }

        return item
    }

    fun air(): Item {
        return get(ItemIds.AIR.id, 0, 0)
    }

    fun isRegistered(id: Int, variant: Int = 0): Boolean {
        if (id < 256) {
            return BlockFactory.isRegistered(id)
        }

        return list.containsKey(getListOffset(id, variant))
    }

    private fun getListOffset(id: Int, variant: Int): Int {
        if (id !in -0x8000..0x7fff) {
            throw IllegalArgumentException("ID must be in range ${0x8000} - ${0x7fff}")
        }

        return ((id and 0xffff) shl 16) or (variant and 0xffff)
    }
}
