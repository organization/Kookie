package be.zvz.kookie.event

import be.zvz.kookie.plugin.Plugin
import be.zvz.kookie.plugin.PluginManager
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe

class EventEmitter<EventType : Event>(
    private val eventClass: Class<EventType>,
    private val handleCancelled: Boolean,
    private val eventPriority: EventPriority,
    private val pluginManager: PluginManager,
    private val plugin: Plugin,
) : ObservableOnSubscribe<EventType> {
    val listener = object : Listener {}

    override fun subscribe(emitter: ObservableEmitter<EventType>) {
        pluginManager.registerEvent(
            eventClass,
            listener,
            { _, it ->
                if (eventClass.isAssignableFrom(it::class.java)) {
                    emitter.onNext(eventClass.cast(it))
                }
            },
            eventPriority,
            plugin,
            handleCancelled
        )
    }
}
