package be.zvz.kookie.event.inventory

import be.zvz.kookie.event.HandlerList
import be.zvz.kookie.inventory.Inventory
import be.zvz.kookie.player.Player

class InventoryCloseEvent(inventory: Inventory, val who: Player) : InventoryEvent(inventory) {
    override val handlers: HandlerList
        get() = handlerList

    companion object {
        private val handlerList = HandlerList(InventoryCloseEvent::class.java)
    }
}
