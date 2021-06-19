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
