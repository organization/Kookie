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
package be.zvz.kookie.network.mcpe.compression

private typealias CallBack = (CompressBatchPromise) -> Unit

class CompressBatchPromise {
    val callbacks: MutableList<CallBack> = mutableListOf()

    var result: Any? = null
        get() {
            assertNotCancelled()

            return field
        }

    var isCancelled: Boolean = false
        private set

    fun hasResult(): Boolean = result !== null

    fun onResolve(callback: CallBack) {
        assertNotCancelled()
        if (result !== null) {
            callback(this)
        } else {
            this.callbacks.add(callback)
        }
    }

    fun onResolve(callbacks: List<CallBack>) {
        assertNotCancelled()
        if (result !== null) {
            callbacks.forEach { it(this) }
        } else {
            this.callbacks.addAll(callbacks)
        }
    }

    fun resolve(result: Any) {
        if (!isCancelled) {
            assertResultIsNull()

            this.result = result

            callbacks.forEach { it(this) }
            callbacks.clear()
        }
    }

    fun cancel() {
        assertResultIsNull()
        isCancelled = true
    }

    private fun assertNotCancelled() {
        if (isCancelled) {
            throw IllegalStateException("Promise has been cancelled")
        }
    }

    private fun assertResultIsNull() {
        if (result !== null) {
            throw IllegalStateException("Promise has been resolved")
        }
    }
}
