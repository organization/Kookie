package be.zvz.kookie.event.inventory

import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.inventory.Inventory
import be.zvz.kookie.player.Player

class InventoryOpenEvent(inventory: Inventory, val who: Player) : InventoryEvent(inventory), Cancellable {
    override var isCancelled: Boolean = false
}
