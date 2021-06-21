package be.zvz.kookie.event.inventory

import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.event.Event

class CraftItemEvent : Event(), Cancellable {
    override var isCancelled: Boolean = false
}
