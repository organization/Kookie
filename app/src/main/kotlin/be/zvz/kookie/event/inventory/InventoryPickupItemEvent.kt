package be.zvz.kookie.event.inventory

import be.zvz.kookie.entity.Entity
import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.event.HandlerList
import be.zvz.kookie.inventory.Inventory

class InventoryPickupItemEvent(inventory: Inventory, val itemEntity: Entity) : InventoryEvent(inventory), Cancellable {
    override var isCancelled: Boolean = false
    override val handlers: HandlerList
        get() = handlerList

    companion object {
        private val handlerList = HandlerList(InventoryPickupItemEvent::class.java)
    }
}
