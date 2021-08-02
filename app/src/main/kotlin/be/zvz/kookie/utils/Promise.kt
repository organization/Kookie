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
package be.zvz.kookie.utils

class Promise<T> {
    private val onSuccessHandlers: MutableList<(T) -> Unit> = mutableListOf()
    private val onFailureHandlers: MutableList<() -> Unit> = mutableListOf()
    private var resolved: Boolean = false

    private var result: T? = null

    fun onCompletion(onSuccess: (T) -> Unit, onFailure: () -> Unit) {
        if (resolved) {
            result?.let(onSuccess) ?: run(onFailure)
        } else {
            onSuccessHandlers.add(onSuccess)
            onFailureHandlers.add(onFailure)
        }
    }

    fun resolve(value: T) {
        if (resolved) {
            throw IllegalStateException("Promise has already been resolved/rejected")
        }
        result = value
        resolved = true

        onSuccessHandlers.forEach { it(value) }
        onSuccessHandlers.clear()
        onFailureHandlers.clear()
    }

    fun reject() {
        if (resolved) {
            throw IllegalStateException("Promise has already been resolved/rejected")
        }
        resolved = true

        onFailureHandlers.forEach { it() }
        onSuccessHandlers.clear()
        onFailureHandlers.clear()
    }
}
