/**
 *  _  __           _    _
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
package be.zvz.kookie.scheduler

import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException

abstract class AsyncTask<T> @JvmOverloads constructor(
    private val onRun: (() -> T)? = null,
    private val onError: ((Exception) -> Unit)? = null
) : Callable<T> {
    private var submitted: Boolean = false

    fun setSubmitted() {
        if (submitted) {
            throw IllegalArgumentException("Cannot submit the same AsyncTask instance more than once")
        } else {
            this.submitted = true
        }
    }

    @Throws(ExecutionException::class)
    override fun call(): T {
        try {
            val result = onRun()
            this.onCompletion(result)
            return result
        } catch (e: Exception) {
            onError(e)
            throw ExecutionException(e)
        }
    }

    @Throws(ExecutionException::class)
    open fun onRun(): T {
        if (onRun === null) {
            throw ExecutionException(IllegalArgumentException("Must override this method or given constructor argument."))
        } else {
            return onRun()
        }
    }

    open fun onError(e: Exception) {
        if (onRun === null) {
            e.printStackTrace()
        } else {
            return onError(e)
        }
    }

    open fun onCompletion(result: T) {
        // EMPTY BODY
    }
}
