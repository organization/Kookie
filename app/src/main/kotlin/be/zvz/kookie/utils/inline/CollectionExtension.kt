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
 * Copyright (C) 2021 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.utils.inline

/**
 * Performs the given [action] on each key and value.
 * Unlike [Map.forEach], we provide [K] and [V] as arguments instead of [Map.Entry].
 */
inline fun <K, V> Map<out K, V>.forEachIndexed(action: (K, V) -> Unit) {
    for (element in this) action(element.key, element.value)
}

/**
 * Performs the given [actionK] and [actionV] on each key and value.
 * Unlike [Map.forEach], we provide [K] and [V] as arguments instead of [Map.Entry].
 */
inline fun <K, V> Map<out K, V>.forEachIndexed(actionK: (K) -> Unit, actionV: (V) -> Unit) {
    for (element in this) {
        actionK(element.key)
        actionV(element.value)
    }
}

/**
 * Performs the given [action] on each key
 * Unlike [Map.forEach], we provide [K] as arguments instead of [Map.Entry].
 */
inline fun <K, V> Map<out K, V>.forEachKey(action: (K) -> Unit) {
    for (element in this) action(element.key)
}

/**
 * Performs the given [action] on each key (given twice)
 * Unlike [Map.forEach], we provide [K] and [K] as arguments instead of [Map.Entry].
 */
inline fun <K, V> Map<out K, V>.forEachKeys(action: (K, K) -> Unit) {
    for (element in this) action(element.key, element.key)
}

/**
 * Performs the given [action] on each value
 * Unlike [Map.forEach], we provide [V] as arguments instead of [Map.Entry].
 */
inline fun <K, V> Map<out K, V>.forEachValue(action: (V) -> Unit) {
    for (element in this) action(element.value)
}

/**
 * Performs the given [action] on each value (given twice)
 * Unlike [Map.forEach], we provide [V] and [V] as arguments instead of [Map.Entry].
 */
inline fun <K, V> Map<out K, V>.forEachValues(action: (V, V) -> Unit) {
    for (element in this) action(element.value, element.value)
}

/**
 * Returns the first key matching the given predicate, or null if no such element was found.
 * Unlike [Map.firstNotNullOfOrNull], we returns [K] as arguments instead of [Map.Entry].
 */
inline fun <K, V> Map<out K, V>.search(predicate: (K, V) -> Boolean): K? {
    this.forEachIndexed { k, v -> if (predicate(k, v)) return k }
    return null
}

/**
 * Returns the first key matching the given predicate, or null if no such element was found.
 * Unlike [Map.search], we provide [K] as arguments instead of [K] and [V].
 */
inline fun <K, V> Map<out K, V>.searchByKey(predicate: (K) -> Boolean): K? {
    this.forEachKey { k -> if (predicate(k)) return k }
    return null
}

/** Returns the first key matching the given value, or null if no such element was found.*/
inline fun <K, V> Map<out K, V>.searchByKey(find: K): K? = this.searchByKey { it == find }

/**
 * Returns the first key matching the given predicate, or null if no such element was found.
 * Unlike [Map.search], we provide [V] as arguments instead of [K] and [V].
 */
inline fun <K, V> Map<out K, V>.searchByValue(predicate: (V) -> Boolean): K? {
    this.forEachIndexed { k, v -> if (predicate(v)) return k }
    return null
}

/** Returns the first key matching the given value, or null if no such element was found.*/
inline fun <K, V> Map<out K, V>.searchByValue(find: V): K? = this.searchByValue { it == find }

/**
 * Returns the first value matching the given predicate, or null if no such element was found.
 * Unlike [Map.firstNotNullOfOrNull], we returns [V] as arguments instead of [Map.Entry].
 */
inline fun <K, V> Map<out K, V>.find(predicate: (K, V) -> Boolean): V? {
    this.forEachIndexed { k, v -> if (predicate(k, v)) return v }
    return null
}

/**
 * Returns the first value matching the given predicate, or null if no such element was found.
 * Unlike [Map.find], we provide [K] as arguments instead of [K] and [V].
 */
inline fun <K, V> Map<out K, V>.findByKey(predicate: (K) -> Boolean): V? {
    this.forEachIndexed { k, v -> if (predicate(k)) return v }
    return null
}

/** Returns the first value matching the given value, or null if no such element was found.*/
inline fun <K, V> Map<out K, V>.findByKey(find: K): V? = this.findByKey { it == find }

/**
 * Returns the first value matching the given predicate, or null if no such element was found.
 * Unlike [Map.find], we provide [V] as arguments instead of [K] and [V].
 */
inline fun <K, V> Map<out K, V>.findByValue(predicate: (V) -> Boolean): V? {
    this.forEachValue { v -> if (predicate(v)) return v }
    return null
}

/** Returns the first value matching the given value, or null if no such element was found.*/
inline fun <K, V> Map<out K, V>.findByValue(find: V): V? = this.findByValue { it == find }
