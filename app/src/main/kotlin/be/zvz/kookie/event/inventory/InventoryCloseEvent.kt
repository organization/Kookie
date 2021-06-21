package be.zvz.kookie.event.inventory

import be.zvz.kookie.inventory.Inventory
import be.zvz.kookie.player.Player

class InventoryCloseEvent(inventory: Inventory, val who: Player) : InventoryEvent(inventory)
