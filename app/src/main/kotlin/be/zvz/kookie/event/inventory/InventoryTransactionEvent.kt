package be.zvz.kookie.event.inventory

import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.event.Event
import be.zvz.kookie.event.HandlerList
import be.zvz.kookie.inventory.transaction.InventoryTransaction

class InventoryTransactionEvent(val transaction: InventoryTransaction) : Event(), Cancellable {
    override var isCancelled: Boolean = false
    override val handlers: HandlerList
        get() = handlerList

    companion object {
        private val handlerList = HandlerList(InventoryTransactionEvent::class.java)
    }
}
