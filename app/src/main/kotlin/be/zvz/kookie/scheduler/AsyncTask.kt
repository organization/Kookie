package be.zvz.kookie.scheduler

import java.util.concurrent.Callable

abstract class AsyncTask<T> : Callable<T> {
    private var submitted = false

    final override fun call(): T {
        return onRun()
    }

    abstract fun onRun(): T

    fun setSubmitted() {
        submitted = true
    }

    fun isSubmitted() = submitted
}
