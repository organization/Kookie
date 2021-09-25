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
import be.zvz.kookie.item.Item
import be.zvz.kookie.network.mcpe.protocol.types.inventory.ContainerIds
import be.zvz.kookie.player.Player
import com.koloboke.collect.map.hash.HashIntObjMaps
import com.koloboke.collect.set.hash.HashObjSets
import com.nukkitx.protocol.bedrock.BedrockPacket

class InventoryManager(
    val player: Player,
    val session: NetworkSession
) {

    val windowMap: MutableMap<Int, Inventory> = HashIntObjMaps.newMutableMap()

    val lastInventoryNetworkId: Int = ContainerIds.FIRST.id

    val initiatedSlotChanges: MutableMap<Int, MutableMap<Int, Item>> = HashIntObjMaps.newMutableMap()

    var clientSelectedHotbarSlot = -1

    val containerOpenCallbacks: MutableSet<(Int, Inventory) -> List<BedrockPacket>?> = HashObjSets.newMutableSet()

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

    companion object {
        // TODO: HACK!
        // these IDs are used for 1.16 to restore 1.14ish crafting & inventory behaviour; since they don't seem to have any
        // effect on the behaviour of inventory transactions I don't currently plan to integrate these into the main system.
        private val RESERVED_WINDOW_ID_RANGE_START = ContainerIds.LAST.id - 10
        private val RESERVED_WINDOW_ID_RANGE_END = ContainerIds.LAST.id

        val HARDCODED_CRAFTING_GRID_WINDOW_ID = RESERVED_WINDOW_ID_RANGE_START + 1
        val HARDCODED_INVENTORY_WINDOW_ID = RESERVED_WINDOW_ID_RANGE_START + 2

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
