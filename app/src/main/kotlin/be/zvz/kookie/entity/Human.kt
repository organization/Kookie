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

import be.zvz.kookie.inventory.CallbackInventoryListener
import be.zvz.kookie.inventory.PlayerEnderInventory
import be.zvz.kookie.inventory.PlayerInventory
import be.zvz.kookie.inventory.PlayerOffHandInventory
import be.zvz.kookie.item.Item
import be.zvz.kookie.item.enchantment.VanillaEnchantments
import be.zvz.kookie.math.Math
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.nbt.NBT
import be.zvz.kookie.nbt.tag.ByteArrayTag
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.IntTag
import be.zvz.kookie.nbt.tag.ListTag
import be.zvz.kookie.nbt.tag.StringTag
import be.zvz.kookie.network.mcpe.convert.SkinAdapterSingleton
import be.zvz.kookie.network.mcpe.convert.TypeConverter
import be.zvz.kookie.network.mcpe.protocol.AddPlayerPacket
import be.zvz.kookie.network.mcpe.protocol.PlayerListPacket
import be.zvz.kookie.network.mcpe.protocol.types.PlayerListEntry
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityIds
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityMetadataProperties
import be.zvz.kookie.network.mcpe.protocol.types.entity.StringMetadataProperty
import be.zvz.kookie.network.mcpe.protocol.types.inventory.ItemStackWrapper
import be.zvz.kookie.player.Player
import java.nio.charset.StandardCharsets
import java.util.UUID
import kotlin.math.min

open class Human(var skin: Skin, location: Location) : Living(location) {

    override val entityNetworkIdentifier = EntityIds.PLAYER

    override val initialSizeInfo: EntitySizeInfo
        get() = EntitySizeInfo(1.8F, 0.6F, 1.62F)

    open lateinit var inventory: PlayerInventory

    open lateinit var offHandInventory: PlayerOffHandInventory

    open lateinit var enderInventory: PlayerEnderInventory

    lateinit var uuid: UUID

    lateinit var hungerManager: HungerManager

    lateinit var xpManager: ExperienceManager

    var xpSeed: Int = 0

    fun sendSkin(targets: List<Player>) {
        /* TODO: server.broadcastPackets
        server.broadcastPackets(
            targets.ifEmpty {
                getViewers()
            },
            PlayerSkinPacket.create(uuid, SkinAdapterSingleton.adapter!!.toSkinData(skin))
        )
        */
    }

    override fun jump() {
        super.jump()
        if (isSprinting()) {
            hungerManager.exhaust(0.8F, 10)
        } else {
            hungerManager.exhaust(0.2F, 9)
        }
    }

    override fun consumeObject(consumable: Consumable): Boolean {
        if (consumable is FoodSource && consumable.requiresHunger() && !hungerManager.isHungry()) {
            return false
        }
        return super.consumeObject(consumable)
    }

    override fun applyConsumptionResult(consumable: Consumable) {
        if (consumable is FoodSource) {
            hungerManager.addFood(consumable.foodRestore.toFloat())
            hungerManager.addSaturation(consumable.saturationRestore)
        }
        super.applyConsumptionResult(consumable)
    }

    override fun getXpDropAmount(): Int {
        return min(100, 7 * xpManager.getXpLevel())
    }

    fun initHumanData(nbt: CompoundTag) {
        val nameTag = nbt.getTag("NameTag")
        if (nameTag is StringTag) {
            this.nameTag = nameTag.value
        }
        // TODO: is this correct?
        uuid = UUID.nameUUIDFromBytes(
            (getId().toString() + skin.skinData + this.nameTag).toByteArray(StandardCharsets.UTF_8)
        )
    }

    override fun initEntity(nbt: CompoundTag) {
        super.initEntity(nbt)
        hungerManager = HungerManager(this)
        xpManager = ExperienceManager(this)

        inventory = PlayerInventory(this)

        val syncHeldItem = {
            getViewers().forEach { (_, player) ->
                // TODO: player.session.onMobMainHandItemChange(this)
            }
        }

        inventory.getListeners().add(
            CallbackInventoryListener(
                { _, slot, _ ->
                    if (slot == inventory.getHeldItemIndex()) {
                        syncHeldItem()
                    }
                },
                { _, oldContents ->
                    if (oldContents.containsKey(inventory.getHeldItemIndex())) {
                        syncHeldItem()
                    }
                }
            )
        )
        offHandInventory = PlayerOffHandInventory(this)
        enderInventory = PlayerEnderInventory(this)
        initHumanData(nbt)
        val inventoryTag = nbt.getListTag("Inventory")
        inventoryTag?.let {
            val armorListeners = armorInventory.getListeners()
            armorInventory.clearListeners()
            val inventoryListeners = inventory.getListeners()
            inventory.clearListeners()

            inventoryTag.value.forEach { item_ ->
                val itemNBT = (item_ as CompoundTag)
                val slot = nbt.getByte("Slot")

                if (slot in 0..8) {
                    // Old hotbar saving stuff, ignore it
                } else if (slot in 100..103) {
                    armorInventory.setItem(slot - 100, Item.nbtDeserialize(itemNBT))
                } else if (slot >= 9 && slot < inventory.size + 9) {
                    inventory.setItem(slot - 9, Item.nbtDeserialize(itemNBT))
                }
            }
            armorInventory.getListeners().addAll(armorListeners)
            inventory.getListeners().addAll(inventoryListeners)
        }
        val offHand = nbt.getCompoundTag("OffHandItem")
        offHand?.let {
            offHandInventory.setItem(0, Item.nbtDeserialize(offHand))
        }
        offHandInventory.getListeners().add(
            CallbackInventoryListener.onAnyChange {
                getViewers().forEach { (_, player) ->
                    // TODO: player.session.onMobOffHandItemChange(this)
                }
            }
        )
        val enderChestInventoryTag = nbt.getListTag("EnderChestInventory")
        enderChestInventoryTag?.let { it ->
            it.value.forEach {
                val tag = it as CompoundTag
                enderInventory.setItem(tag.getByte("Slot"), Item.nbtDeserialize(tag))
            }
        }
        inventory.setHeldItemIndex(nbt.getInt("SelectedInventorySlot", 0))
        inventory.heldItemIndexChangeListeners.add {
            getViewers().forEach { (_, player) ->
                // TODO: player.session.onMobMainHandItemChange(this)
            }
        }
        hungerManager.setFood(nbt.getInt("foodLevel", hungerManager.getFood().toInt()).toFloat())
        hungerManager.setExhaustion(nbt.getFloat("foodExhaustionLevel", hungerManager.getExhaustion()))
        hungerManager.setSaturation(nbt.getFloat("foodSaturationLevel", hungerManager.getSaturation()))
        hungerManager.foodTickTimer = nbt.getLong("foodTickTimer", hungerManager.foodTickTimer)
        xpManager.setXpAndProgressNoEvent(
            nbt.getInt("XpLevel", 0),
            nbt.getFloat("XpP", 0F)
        )
        xpManager.totalXp = nbt.getInt("XpTotal", 0)

        val xpSeedTag = nbt.getTag("XpSeed")
        xpSeed = if (xpSeedTag is IntTag) {
            xpSeedTag.value
        } else {
            Math.random(-0x7fffffff - 1, 0x7fffffff)
        }
    }

    override fun entityBaseTick(tickDiff: Long): Boolean {
        val hasUpdate = super.entityBaseTick(tickDiff)
        hungerManager.tick(tickDiff)
        xpManager.tick(tickDiff)
        return hasUpdate
    }

    override fun applyDamageModifiers(source: Any) {
        super.applyDamageModifiers(source)
        // TODO
    }

    override fun applyPostDamageEffects(source: Any) {
        super.applyPostDamageEffects(source)
        // TODO
    }

    override fun getDrops(): MutableList<Item> {
        val inventoryContents = inventory.getContents(false).filter { (_, item) ->
            item.hasEnchantment(VanillaEnchantments.VANISHING.enchantment)
        }
        val armorInventoryContents = armorInventory.getContents(false).filter { (_, item) ->
            item.hasEnchantment(VanillaEnchantments.VANISHING.enchantment)
        }
        val offHandInventoryContents = offHandInventory.getContents(false).filter { (_, item) ->
            item.hasEnchantment(VanillaEnchantments.VANISHING.enchantment)
        }
        return mutableListOf<Item>().apply {
            inventoryContents.forEach { (_, item) -> add(item) }
            armorInventoryContents.forEach { (_, item) -> add(item) }
            offHandInventoryContents.forEach { (_, item) -> add(item) }
        }
    }

    override fun saveNBT(): CompoundTag {
        val nbt = super.saveNBT()
        nbt.setInt("foodLevel", hungerManager.getFood().toInt())
        nbt.setFloat("foodExhaustionLevel", hungerManager.getExhaustion())
        nbt.setFloat("foodSaturationLevel", hungerManager.getSaturation())
        nbt.setLong("foodTickTimer", hungerManager.foodTickTimer)

        nbt.setInt("XpLevel", xpManager.getXpLevel())
        nbt.setFloat("XpP", xpManager.getXpProgress())
        nbt.setInt("XpTotal", xpManager.getLifetimeTotalXp())
        nbt.setInt("XpSeed", xpSeed)

        val inventoryTag = ListTag<CompoundTag>(mutableListOf(), NBT.TagType.COMPOUND)
        nbt.setTag("Inventory", inventoryTag)

        var slotCount = inventory.size + inventory.getHotbarSize()
        repeat(slotCount) { i ->
            val item = inventory.getItem(i - 9)
            if (!item.isNull()) {
                inventoryTag.push(item.nbtSerialize(i))
            }
        }
        for (i in 100 until 104) {
            val item = armorInventory.getItem(i - 100)
            if (!item.isNull()) {
                inventoryTag.push(item.nbtSerialize(i))
            }
        }
        val offHandItem = offHandInventory.getItem(0)
        if (!offHandItem.isNull()) {
            nbt.setTag("OffHandItem", offHandItem.nbtSerialize())
        }
        val items: MutableList<CompoundTag> = mutableListOf()

        slotCount = enderInventory.size
        repeat(slotCount) { i ->
            val item = enderInventory.getItem(i)
            if (!item.isNull()) {
                items.add(item.nbtSerialize(i))
            }
        }
        nbt.setTag("EnderChestInventory", ListTag(items, NBT.TagType.COMPOUND))
        nbt.setTag(
            "Skin",
            CompoundTag.create().apply {
                setString("Name", skin.skinId)
                setTag("Data", ByteArrayTag(skin.skinData.toByteArray()))
                setTag("CapeData", ByteArrayTag(skin.capeData.toByteArray()))
                setString("GeometryName", skin.geometryName)
                setTag("GeometryData", ByteArrayTag(skin.geometryData.toByteArray()))
            }
        )
        return nbt
    }

    override fun spawnTo(player: Player) {
        if (player != this) {
            super.spawnTo(player)
        }
    }

    override fun sendSpawnPacket(player: Player) {
        if (this !is Player) {
            player.session.sendDataPacket(
                PlayerListPacket.add(
                    listOf(
                        PlayerListEntry.createAdditionEntry(
                            uuid,
                            getId(),
                            nameTag,
                            SkinAdapterSingleton.adapter!!.toSkinData(skin)
                        )
                    )
                )
            )
        }
        if (this !is Player) {
            player.session.sendDataPacket(
                AddPlayerPacket().also {
                    it.uuid = uuid
                    it.username = nameTag
                    it.entityRuntimeId = getId()
                    it.position = location.asVector3()
                    it.motion = motion
                    it.yaw = location.yaw.toFloat()
                    it.pitch = location.pitch.toFloat()
                    it.item = ItemStackWrapper.legacy(TypeConverter.coreItemStackToNet(inventory.getItemInHand()))
                    it.metadata = getAllNetworkData()

                    sendData(
                        mutableMapOf(player.getId() to player),
                        mutableMapOf(EntityMetadataProperties.NAMETAG to StringMetadataProperty(nameTag))
                    )

                    // TODO: player.session.onMobArmorChange(this)
                    // TODO: player.session.onMobOffHandItemChange(this)
                }
            )
        }
    }

    override fun getOffsetPosition(vector: Vector3): Vector3 = vector.add(0.0, 1.621, 0.0)

    companion object {
        @JvmStatic
        fun parseSkinNBT(nbt: CompoundTag): Skin =
            nbt.getCompoundTag("Skin")?.let {
                Skin(
                    it.getString("Name"),
                    it.getTag("Data")?.value as String,
                    it.getTag("CapeData")?.value as String,
                    it.getString("GeometryName"),
                    it.getTag("GeometryData")?.value as String
                )
            } ?: throw IllegalStateException("Missing skin data")
    }
}
