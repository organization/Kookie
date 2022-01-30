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
package be.zvz.kookie.network.mcpe

import be.zvz.kookie.inventory.Inventory
import be.zvz.kookie.inventory.transaction.InventoryTransaction
import be.zvz.kookie.inventory.transaction.action.SlotChangeAction
import be.zvz.kookie.item.Item
import be.zvz.kookie.network.mcpe.convert.KookieToNukkitProtocolConverter
import be.zvz.kookie.network.mcpe.convert.TypeConverter
import be.zvz.kookie.network.mcpe.protocol.types.inventory.ContainerIds
import be.zvz.kookie.network.mcpe.protocol.types.inventory.ItemStackWrapper
import be.zvz.kookie.network.mcpe.protocol.types.inventory.WindowTypes
import be.zvz.kookie.player.Player
import com.koloboke.collect.map.hash.HashIntObjMaps
import com.koloboke.collect.set.hash.HashObjSets
import com.nukkitx.protocol.bedrock.BedrockPacket
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType
import com.nukkitx.protocol.bedrock.packet.ContainerClosePacket
import com.nukkitx.protocol.bedrock.packet.ContainerOpenPacket
import com.nukkitx.protocol.bedrock.packet.ContainerSetDataPacket
import com.nukkitx.protocol.bedrock.packet.CreativeContentPacket
import com.nukkitx.protocol.bedrock.packet.InventoryContentPacket
import com.nukkitx.protocol.bedrock.packet.InventorySlotPacket
import com.nukkitx.protocol.bedrock.packet.MobEquipmentPacket
import kotlin.math.max

class InventoryManager(
    val player: Player,
    val session: NetworkSession
) {

    val windowMap: MutableMap<Int, Inventory> = HashIntObjMaps.newMutableMap()

    var lastInventoryNetworkId: Int = ContainerIds.FIRST.id

    val initiatedSlotChanges: MutableMap<Int, MutableMap<Int, Item>> = HashIntObjMaps.newMutableMap()

    var clientSelectedHotbarSlot = -1

    val containerOpenCallbacks: MutableSet<(Int, Inventory) -> List<BedrockPacket>?> = HashObjSets.newMutableSet()

    val openHardcodedWindows: MutableMap<Int, Boolean> = HashIntObjMaps.newMutableMap()

    init {
        /*
        $this->containerOpenCallbacks = new ObjectSet();
		$this->containerOpenCallbacks->add(\Closure::fromCallable([self::class, 'createContainerOpen']));

		$this->add(ContainerIds::INVENTORY, $this->player->getInventory());
		$this->add(ContainerIds::OFFHAND, $this->player->getOffHandInventory());
		$this->add(ContainerIds::ARMOR, $this->player->getArmorInventory());
		$this->add(ContainerIds::UI, $this->player->getCursorInventory());

		$this->player->getInventory()->getHeldItemIndexChangeListeners()->add(function() : void{
			$this->syncSelectedHotbarSlot();
		});
         */
        containerOpenCallbacks.add(::createContainerOpen)
    }

    fun add(id: Int, inventory: Inventory) {
        windowMap[id] = inventory
    }

    fun remove(id: Int) {
        windowMap.remove(id)
        initiatedSlotChanges.remove(id)
    }

    fun getWindowId(inventory: Inventory): Int? {
        for ((key, value) in windowMap) {
            if (value == inventory) {
                return key
            }
        }
        return null
    }

    fun getCurrentWindowId(): Int {
        return lastInventoryNetworkId
    }

    fun getWindow(windowId: Int): Inventory? {
        return windowMap[windowId]
    }

    fun onTransactionStart(tx: InventoryTransaction) {
        tx.actions.forEach {
            if (it is SlotChangeAction) {
                val windowId = getWindowId(it.inventory)
                windowId?.let { id ->
                    initiatedSlotChanges.getOrPut(id, ::mutableMapOf)[it.slot] = it.targetItem
                }
            }
        }
    }

    fun onCurrentWindowChange(inventory: Inventory) {
        // onCurrentWindowRemove() TODO
        lastInventoryNetworkId = max(ContainerIds.FIRST.id, (lastInventoryNetworkId + 1) % RESERVED_WINDOW_ID_RANGE_START)
        add(lastInventoryNetworkId, inventory)

        containerOpenCallbacks.forEach {
            val packets = it(lastInventoryNetworkId, inventory)
            packets?.forEach {
                session.sendDataPacket(it)
            }
            // syncContents(inventory) TODO
        }
    }

    fun onClientOpenMainInventory() {
        val id = HARDCODED_INVENTORY_WINDOW_ID
        if (!openHardcodedWindows.containsKey(id)) {
            openHardcodedWindows[id] = true
            session.sendDataPacket(
                ContainerOpenPacket().apply {
                    this.id = HARDCODED_INVENTORY_WINDOW_ID.toByte()
                    this.type = ContainerType.from(WindowTypes.INVENTORY.value)
                    this.uniqueEntityId = player.getId()
                }
            )
        }
    }

    fun onCurrentWindowRemove() {
        if (windowMap.containsKey(lastInventoryNetworkId)) {
            remove(lastInventoryNetworkId)
            session.sendDataPacket(
                ContainerClosePacket().apply {
                    this.id = lastInventoryNetworkId.toByte()
                    this.isUnknownBool0 = true
                }
            )
        }
    }

    fun onClientRemoveWindow(id: Int) {
        if (openHardcodedWindows.containsKey(id)) {
            openHardcodedWindows.remove(id)
        } else if (id == lastInventoryNetworkId) {
            remove(id)
            // TODO: uncomment this when we implements Player.removeCurrentWindow()
            //  player.removeCurrentWindow()
        } else {
            session.logger.debug("Attempted to close inventory with network ID $id, but current is $lastInventoryNetworkId")
        }
        // Always send this, even if no window matches. If we told the client to close a window, it will behave as if it
        // initiated the close and expect an ack.
        session.sendDataPacket(
            ContainerClosePacket().apply {
                this.id = id.toByte()
                this.isUnknownBool0 = false
            }
        )
    }

    fun syncSlot(inventory: Inventory, slot: Int) {
        val windowId = getWindowId(inventory)
        if (windowId != null) {
            val currentItem = inventory.getItem(slot)
            val clientSideItem = initiatedSlotChanges[windowId]?.get(slot) ?: null
            if (clientSideItem == null || !clientSideItem.equalsExact(currentItem)) {
                val itemStackWrapper = ItemStackWrapper.legacy(TypeConverter.coreItemStackToNet(currentItem))
                if (windowId == ContainerIds.OFFHAND.id) {
                    // TODO: HACK!
                    // The client may sometimes ignore the InventorySlotPacket for the offhand slot.
                    // This can cause a lot of problems (totems, arrows, and more...).
                    // The workaround is to send an InventoryContentPacket instead
                    // BDS (Bedrock Dedicated Server) also seems to work this way.
                    session.sendDataPacket(
                        InventoryContentPacket().apply {
                            this.containerId = windowId
                            this.contents = listOf(KookieToNukkitProtocolConverter.toItemData(itemStackWrapper))
                        }
                    )
                }
            }
        }
    }

    fun syncContents(inventory: Inventory) {
        val windowId = getWindowId(inventory)
        if (windowId != null) {
            initiatedSlotChanges.remove(windowId)
            if (windowId == ContainerIds.UI.id) {
                // TODO: HACK!
                // Since 1.13, cursor is now part of a larger "UI inventory", and sending contents for this larger inventory does
                // not work the way it's intended to. Even if it did, it would be necessary to send all 51 slots just to update
                // this one, which is just not worth it.
                // This workaround isn't great, but it's at least simple.
                session.sendDataPacket(
                    InventorySlotPacket().apply {
                        this.containerId = windowId
                        this.slot = 0
                        this.item = KookieToNukkitProtocolConverter.toItemData(
                            ItemStackWrapper.legacy(
                                TypeConverter.coreItemStackToNet(inventory.getItem(0))
                            )
                        )
                    }
                )
            } else {
                session.sendDataPacket(
                    InventoryContentPacket().apply {
                        this.containerId = windowId
                        this.contents = inventory.getContents(true).map { (_, item) ->
                            KookieToNukkitProtocolConverter.toItemData(
                                ItemStackWrapper.legacy(
                                    TypeConverter.coreItemStackToNet(item)
                                )
                            )
                        }
                    }
                )
            }
        }
    }

    fun syncAll() {
        windowMap.forEach {
            syncContents(it.value)
        }
    }

    fun syncData(inventory: Inventory, propertyId: Int, value: Int) {
        val windowId = getWindowId(inventory)
        if (windowId != null) {
            session.sendDataPacket(
                ContainerSetDataPacket().apply {
                    this.windowId = windowId.toByte()
                    this.property = propertyId
                    this.value = value
                }
            )
        }
    }

    fun onClientSelectHotbarSlot(slot: Int) {
        clientSelectedHotbarSlot = slot
    }

    fun syncSelectedHotbarSlot() {
        val selected = player.inventory.getHeldItemIndex()
        if (selected != clientSelectedHotbarSlot) {
            session.sendDataPacket(
                MobEquipmentPacket().apply {
                    this.runtimeEntityId = player.getId()
                    this.item = KookieToNukkitProtocolConverter.toItemData(
                        ItemStackWrapper.legacy(
                            TypeConverter.coreItemStackToNet(player.inventory.getItemInHand())
                        )
                    )
                    this.hotbarSlot = selected
                    this.inventorySlot = selected
                    this.containerId = ContainerIds.INVENTORY.id
                }
            )
        }
    }

    fun syncCreative() {
        // TODO: Implement this when we have proper Player implementation
        var nextEntryId = 1
        session.sendDataPacket(
            CreativeContentPacket().apply {
                /*
                if (player.isSpectator()) {
                    this.contents = listOf<ItemData>()
                } else {
                    this.contents = CreativeInventory::getInstance()->getAll().map { (_, item) ->
                        KookieToNukkitProtocolConverter.toItemData(
                            ItemStackWrapper.legacy(
                                TypeConverter.coreItemStackToNet(item)
                            )
                        )
                    }
                }
                 */
            }
        )
    }

    companion object {
        // TODO: HACK!
        // these IDs are used for 1.16 to restore 1.14ish crafting & inventory behaviour; since they don't seem to have any
        // effect on the behaviour of inventory transactions I don't currently plan to integrate these into the main system.
        private val RESERVED_WINDOW_ID_RANGE_START = ContainerIds.LAST.id - 10
        private val HARDCODED_INVENTORY_WINDOW_ID = RESERVED_WINDOW_ID_RANGE_START + 2

        private fun createContainerOpen(id: Int, inv: Inventory): List<BedrockPacket>? {
            /*
            if (inv is BlockInventory) {
                when (true) {
                    inv is LoomInventory -> {
                        return listOf(ContainerOpenPacket().apply {
                            this.id = id.toByte()
                            this.type = ContainerType.LOOM
                            this.blockPosition = Vector3i.from(inv.holder.floorX, inv.holder.floorY, inv.holder.floorZ)
                        })
                    }
                    inv is FurnaceInventory -> {
                        return when (inv.furnanceType.id) {
                            FurnaceType.FURNACE.id -> listOf(ContainerOpenPacket().apply {
                                this.id = id.toByte()
                                this.type = ContainerType.FURNACE
                                this.blockPosition = Vector3i.from(inv.holder.floorX, inv.holder.floorY, inv.holder.floorZ)
                            })
                            FurnaceType.BLAST_FURNACE.id -> listOf(ContainerOpenPacket().apply {
                                this.id = id.toByte()
                                this.type = ContainerType.BLAST_FURNACE
                                this.blockPosition = Vector3i.from(inv.holder.floorX, inv.holder.floorY, inv.holder.floorZ)
                            })
                            FurnaceType.SMOKER.id -> listOf(ContainerOpenPacket().apply {
                                this.id = id.toByte()
                                this.type = ContainerType.SMOKER
                                this.blockPosition = Vector3i.from(inv.holder.floorX, inv.holder.floorY, inv.holder.floorZ)
                            })
                            else -> throw AssertionError("Unreachable")
                        }
                    }
                    inv is EnchantInventory -> {
                        return listOf(ContainerOpenPacket().apply {
                            this.id = id.toByte()
                            this.type = ContainerType.ENCHANTMENT
                            this.blockPosition = Vector3i.from(inv.holder.floorX, inv.holder.floorY, inv.holder.floorZ)
                        })
                    }
                    // TODO: more
                    else -> return listOf(ContainerOpenPacket().apply {
                        this.id = id
                        this.type = ContainerType.CONTAINER
                        this.blockPosition = Vector3i.from(inv.holder.floorX, inv.holder.floorY, inv.holder.floorZ)
                    })
                }
            }

             */
            return null
        }
    }
}
