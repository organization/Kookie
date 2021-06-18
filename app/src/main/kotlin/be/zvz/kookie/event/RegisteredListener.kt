package be.zvz.kookie.event

import be.zvz.kookie.plugin.Plugin
import be.zvz.kookie.timings.TimingsHandler

class RegisteredListener(
    val handler: (Event) -> Unit,
    val priority: EventPriority,
    val plugin: Plugin,
    val handleCancelled: Boolean,
    val timings: TimingsHandler
) {

    fun callEvent(event: Event) {
    }
}
