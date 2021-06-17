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
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.network.mcpe.protocol.types.entity.EntityIds
import java.util.UUID

open class Human(val skin: Skin, location: Location) : Living(location) {

    override val entityNetworkIdentifier = EntityIds.PLAYER

    override fun getInitialSizeInfo(): EntitySizeInfo = EntitySizeInfo(1.8F, 0.6F, 1.62F)

    open lateinit var inventory: PlayerInventory

    open lateinit var offHandInventory: PlayerOffHandInventory

    open lateinit var enderInventory: PlayerEnderInventory

    lateinit var uuid: UUID

    lateinit var hungerManager: HungerManager

    lateinit var xpManager: Any

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
        // TODO: initHumanData(nbt)
        val inventoryTag = nbt.getListTag("Inventory")
    }

    companion object {
        @JvmStatic
        fun parseSkinNBT(nbt: CompoundTag): Skin {
            val skinTag = nbt.getCompoundTag("Skin")
            skinTag.let {
                if (it == null) {
                    throw IllegalStateException("Missing skin data")
                }
                return Skin(
                    it.getString("Name"),
                    it.getTag("Data")?.value as String,
                    it.getTag("CapeData")?.value as String,
                    it.getString("GeometryName"),
                    it.getTag("GeometryData")?.value as String
                )
            }
        }
    }
}
