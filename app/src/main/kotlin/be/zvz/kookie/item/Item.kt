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

import be.zvz.kookie.block.Block
import be.zvz.kookie.block.BlockToolType
import be.zvz.kookie.block.VanillaBlocks
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.item.enchantment.EnchantmentIdMap
import be.zvz.kookie.item.enchantment.EnchantmentInstance
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.nbt.LittleEndianNbtSerializer
import be.zvz.kookie.nbt.NBT
import be.zvz.kookie.nbt.TreeRoot
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.ListTag
import be.zvz.kookie.nbt.tag.ShortTag
import be.zvz.kookie.nbt.tag.StringTag
import be.zvz.kookie.player.Player
import be.zvz.kookie.utils.Binary
import com.koloboke.collect.map.hash.HashIntObjMaps
import com.koloboke.collect.map.hash.HashObjObjMaps
import java.util.Base64

open class Item @JvmOverloads constructor(
    private val identifier: ItemIdentifier,
    protected val vanillaName: String = "Unknown",
) : Cloneable, ItemEnchantmentHandling {

    override val enchantments: MutableMap<Int, EnchantmentInstance> = HashIntObjMaps.newMutableMap()
    private var nbt: CompoundTag = CompoundTag()
    var count: Int = 1
    var customName: String = ""
        set(v) {
            if (v.isNotEmpty()) {
                field = v
            }
        }
    var lore = mutableListOf<String>()
    protected var blockEntityTag: CompoundTag? = null
    protected var canPlaceOn: MutableMap<String, String> = hashMapOf()
        set(value) {
            val map: MutableMap<String, String> = HashObjObjMaps.newMutableMap()
            value.forEach { (_, value) ->
                map[value] = value
            }
            field = map
        }
    protected var canDestroy: MutableMap<String, String> = hashMapOf()
        set(value) {
            val map: MutableMap<String, String> = HashObjObjMaps.newMutableMap()
            value.forEach { (_, value) ->
                map[value] = value
            }
            field = map
        }
    open val maxStackSize: Int = 64
    open val fuelTime: Int = 0
    open val attackPoint: Int = 1
    open val defensePoints: Int = 0
    open val blockToolType = BlockToolType.NONE
    open val blockToolHarvestLevel = 0

    fun hasCustomBlockData(): Boolean = this.blockEntityTag != null

    fun clearCustomBlockData(): Item = this.apply {
        blockEntityTag = null
    }

    fun setCustomBlockData(nbt: CompoundTag): Item = this.apply {
        blockEntityTag = nbt.clone() as CompoundTag
    }

    fun getCustomBlockData(): CompoundTag? = this.blockEntityTag

    fun hasCustomName(): Boolean = customName != ""

    fun hasNamedTag(): Boolean = getNamedTag().count() > 0

    fun getNamedTag(): CompoundTag {
        serializeCompoundTag(nbt)
        return nbt
    }

    fun setNamedTag(tag: CompoundTag): Item = this.apply {
        if (tag.count() == 0) clearNamedTag()
        else {
            nbt = tag.clone() as CompoundTag
            deserializeCompoundTag(nbt)
        }
    }

    fun clearNamedTag(): Item = this.apply {
        nbt = CompoundTag()
        deserializeCompoundTag(nbt)
    }

    protected open fun deserializeCompoundTag(tag: CompoundTag) {
        customName = ""
        lore.clear()

        tag.getCompoundTag(TAG_DISPLAY)?.let { display ->
            customName = display.getString(TAG_DISPLAY_NAME, customName)
            display.getListTag(TAG_DISPLAY_LORE)?.takeIf { it.getTagType() == NBT.TagType.STRING }?.let { loreTag ->
                loreTag.value.forEach {
                    lore.add(it.value.toString())
                }
            }
        }

        removeEnchantments()
        val enchantmentsTag = tag.getListTag(TAG_ENCH)?.takeIf { it.getTagType() == NBT.TagType.COMPOUND }
        enchantmentsTag?.let {
            it.value.forEach { value ->
                value as CompoundTag
                val magicNumber = value.getShort("id", -1)
                val level = value.getShort("lvl", 0)
                if (level <= 0) {
                    return
                }
                EnchantmentIdMap.fromId(magicNumber)?.let { type ->
                    addEnchantment(EnchantmentInstance(type, level))
                }
            }
        }

        blockEntityTag = tag.getCompoundTag(TAG_BLOCK_ENTITY_TAG)

        canPlaceOn.clear()
        tag.getListTag("CanPlaceOn")?.value?.forEach {
            canPlaceOn[it.value.toString()] = it.value.toString()
        }
        canDestroy.clear()
        tag.getListTag("canDestroy")?.value?.forEach {
            canDestroy[it.value.toString()] = it.value.toString()
        }
    }

    protected open fun serializeCompoundTag(tag: CompoundTag) {
        val display = tag.getCompoundTag(TAG_DISPLAY) ?: CompoundTag()

        display.setStringIf(TAG_DISPLAY_NAME, customName) { it.isNotEmpty() }

        val loreTag = ListTag<StringTag>().apply {
            lore.forEach {
                push(StringTag(it))
            }
        }
        display.setTagIf(TAG_DISPLAY_LORE, loreTag) { it.isNotEmpty() }

        tag.setTagIf(TAG_DISPLAY, display) { it.isNotEmpty() }

        val enchTag = ListTag<CompoundTag>().apply {
            enchantments.forEach {
                push(
                    CompoundTag.create()
                        .setShort("id", EnchantmentIdMap.toId(it.value.getType()))
                        .setShort("lvl", it.value.level)
                )
            }
        }
        tag.setTagIf(TAG_ENCH, enchTag) { it.isNotEmpty() }

        tag.setTagIf(TAG_BLOCK_ENTITY_TAG, getCustomBlockData()?.clone())

        val canPlaceOnTag = ListTag<StringTag>().apply {
            canPlaceOn.forEach { (_, value) ->
                push(StringTag(value))
            }
        }
        tag.setTagIf("CanPlaceOn", canPlaceOnTag) { it.isNotEmpty() }

        val canDestroyTag = ListTag<StringTag>().apply {
            canDestroy.forEach { (_, value) ->
                push(StringTag(value))
            }
        }
        tag.setTagIf("canDestroy", canDestroyTag) { it.isNotEmpty() }
    }

    @JvmOverloads
    fun pop(count: Int = 1): Item {
        if (count > this.count) {
            throw Exception("Cannot pop $count items from a stack of ${this.count}")
        }
        val item = clone()
        item.count -= count
        this.count -= count
        return item
    }

    fun isNull(): Boolean = count <= 0 || getId() == 0

    fun getName(): String = if (hasCustomName()) customName else vanillaName

    fun canBePlaced(): Boolean = getBlock().canBePlaced()

    @JvmOverloads
    open fun getBlock(clickedFace: Int? = null): Block = VanillaBlocks.AIR.block

    fun getId(): Int = identifier.id

    open fun getMeta(): Int = identifier.meta

    fun hasAnyDamageValue(): Boolean = identifier.meta == -1

    open fun getFuelResidue(): Item = clone().apply { pop() }

    open fun getMiningEfficiency(isCorrectTool: Boolean): Float = 1F

    open fun onInteractBlock(
        player: Player,
        blockReplace: Block,
        blockClicked: Block,
        face: Int,
        clickVector: Vector3
    ): ItemUseResult = ItemUseResult.NONE

    open fun onClickAir(player: Player, directionVector: Vector3): ItemUseResult = ItemUseResult.NONE

    open fun onReleaseUsing(player: Player): ItemUseResult = ItemUseResult.NONE

    open fun onDestroyBlock(block: Block): Boolean = false

    open fun onAttackEntity(victim: Entity): Boolean = false

    @JvmOverloads
    fun equals(item: Item, checkDamage: Boolean = true, checkCompound: Boolean = true) =
        getId() == item.getId() &&
            (!checkDamage || getMeta() == item.getMeta()) &&
            (!checkCompound || getNamedTag() == item.getNamedTag())

    fun equalsExact(other: Item): Boolean = equals(other) && count == other.count

    override fun toString(): String =
        "Item $vanillaName (${getId()}:${if (hasAnyDamageValue()) "?" else getMeta()})x$count tags:0x" + when {
            hasNamedTag() -> Base64.getEncoder().encodeToString(
                LittleEndianNbtSerializer().write(
                    TreeRoot(getNamedTag())
                ).toByteArray()
            )
            else -> ""
        }

    // TODO: json/nbt (de)serialize
    @JvmOverloads
    fun nbtSerialize(slot: Int = -1): CompoundTag {
        val result = CompoundTag.create()
            .setShort("id", getId())
            .setByte("Count", count)
            .setShort("Damage", getMeta())

        if (hasNamedTag()) {
            result.setTag("tag", getNamedTag())
        }

        if (slot != -1) {
            result.setByte("Slot", slot)
        }

        return result
    }

    public override fun clone(): Item = (super.clone() as Item).also {
        it.nbt = nbt.clone() as CompoundTag
        it.blockEntityTag = blockEntityTag?.clone() as? CompoundTag
    }

    companion object {
        const val TAG_ENCH = "ench"
        const val TAG_DISPLAY = "display"
        const val TAG_BLOCK_ENTITY_TAG = "BlockEntityTag"
        const val TAG_DISPLAY_NAME = "Name"
        const val TAG_DISPLAY_LORE = "Lore"

        @JvmStatic
        fun nbtDeserialize(tag: CompoundTag): Item {
            val idTag = tag.getTag("id")
            if (idTag == null || tag.getTag("Count") == null) {
                return ItemFactory.air()
            }

            val count = Binary.unsignByte(tag.getByte("Count"))
            val meta = tag.getShort("Damage", 0)

            val item: Item = when (idTag) {
                is ShortTag -> ItemFactory.get(idTag.value, meta, count)
                is StringTag -> {
                    val id = LegacyStringToItemParser.parseId(idTag.value)

                    if (id === null) {
                        ItemFactory.air()
                    } else {
                        ItemFactory.get(id, meta, count)
                    }
                }
                else -> throw IllegalArgumentException(
                    "Item CompoundTag ID must be an instance of StringTag or ShortTag, " +
                        "${idTag::class.java.simpleName} given"
                )
            }

            tag.getCompoundTag("tag")?.let {
                item.setNamedTag(it)
            }

            return item
        }
    }
}
