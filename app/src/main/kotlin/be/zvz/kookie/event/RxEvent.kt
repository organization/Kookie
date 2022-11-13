/**
 *
 * _  __           _    _
 * | |/ /___   ___ | | _(_) ___
 * | ' // _ \ / _ \| |/ / |/ _ \
 * | . \ (_) | (_) |   <| |  __/
 * |_|\_\___/ \___/|_|\_\_|\___|
 *
 * A server software for Minecraft: Bedrock Edition
 *
 * Copyright (C) 2021 - 2022 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.event

import be.zvz.kookie.plugin.Plugin
import be.zvz.kookie.plugin.PluginManager
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

/*
 * caroline (https://github.com/FelixKlauke/caroline)
 * MIT License
 *
 * Copyright (c) 2017 Felix Klauke
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
object RxEvent {
    /**
     * The composite disposable to clean up when the plugin gets disabled.
     */
    private val compositeDisposable = CompositeDisposable()

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
