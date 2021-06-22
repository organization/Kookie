package be.zvz.kookie.event

import be.zvz.kookie.plugin.Plugin
import be.zvz.kookie.plugin.PluginManager
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

object RxEvent {
    inline fun <reified EventType : Event> observeEvent(
        handleCancelled: Boolean = false,
        eventPriority: EventPriority = EventPriority.NORMAL,
        pluginManager: PluginManager,
        plugin: Plugin,
    ) = observeEvent(EventType::class.java, eventPriority, handleCancelled, pluginManager, plugin)

    /**
     * Observe on a specific spigot event.
     *
     * @param eventClazz  The class of the event.
     * @param <EventType> The type of the event.
     * @return The observable. */
    fun <EventType : Event> observeEvent(
        eventClazz: Class<EventType>,
        pluginManager: PluginManager,
        plugin: Plugin,
    ): Observable<EventType> =
        observeEvent(eventClazz, EventPriority.NORMAL, pluginManager, plugin)

    /**
     * Observe on a specific event with a given event priority.
     *
     * @param eventClazz    The class of the event.
     * @param eventPriority The event priority.
     * @param <EventType>   The type of the event.
     * @return The observable. */
    fun <EventType : Event> observeEvent(
        eventClazz: Class<EventType>,
        eventPriority: EventPriority,
        pluginManager: PluginManager,
        plugin: Plugin,
    ): Observable<EventType> =
        observeEvent(eventClazz, eventPriority, false, pluginManager, plugin)

    /**
     * Observe on a specific event and ignore cancelled events or not.
     *
     * @param eventClazz      The class of the event.
     * @param handleCancelled If we should handle cancelled events.
     * @param <EventType>     The type of the event.
     * @return The observable. */
    fun <EventType : Event> observeEvent(
        eventClazz: Class<EventType>,
        handleCancelled: Boolean,
        pluginManager: PluginManager,
        plugin: Plugin,
    ): Observable<EventType> =
        observeEvent(eventClazz, EventPriority.NORMAL, handleCancelled, pluginManager, plugin)

    /**
     * Observe on a specific event with a given event priority and ignore cancelled events or not.
     *
     * @param eventClazz      The class of the event.
     * @param eventPriority   The event priority.
     * @param handleCancelled If we should handle cancelled events.
     * @param <EventType>     The type of the event.
     * @return The observable.
     */
    fun <EventType : Event> observeEvent(
        eventClazz: Class<EventType>,
        eventPriority: EventPriority,
        handleCancelled: Boolean,
        pluginManager: PluginManager,
        plugin: Plugin,
    ): Observable<EventType> =
        observeEvent(EventEmitter(eventClazz, handleCancelled, eventPriority, pluginManager, plugin))

    /**
     * The composite disposable to clean up when the plugin gets disabled.
     */
    private val compositeDisposable = CompositeDisposable()

    /**
     * Create an observable based on the given emitter.
     *
     * @param eventEmitter The emitter.
     * @param <EventType> The type of the event.
     * @return The observable.
     */
    private fun <EventType : Event> observeEvent(eventEmitter: EventEmitter<EventType>): Observable<EventType> {
        return Observable.create(eventEmitter)
            .doOnSubscribe(compositeDisposable::add)
            .doOnDispose { HandlerListManager.unregisterAll(eventEmitter.listener) }
    }
}
