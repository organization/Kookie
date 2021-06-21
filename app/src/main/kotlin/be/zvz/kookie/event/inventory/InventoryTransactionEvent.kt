package be.zvz.kookie.event.inventory

import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.event.Event
import be.zvz.kookie.inventory.transaction.InventoryTransaction

class InventoryTransactionEvent(val transaction: InventoryTransaction) : Event(), Cancellable {
    override var isCancelled: Boolean = false
}
