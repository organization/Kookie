package be.zvz.kookie.scheduler

import kotlinx.coroutines.Runnable

abstract class AsyncTask : Runnable {

    private var result: Any? = null

    private var running = false

    private var crashed = false

    private var crashInformation: Throwable? = null

    private var submitted = false

    override fun run() {
        try {
            running = true
            onRun()
            running = false
        } catch (e: Throwable) {
            crashInformation = e
            crashed = true
        }
    }

    abstract fun onRun()

    fun setSubmitted() {
        submitted = true
    }

    fun isSubmitted() = submitted

    fun isCrashed(): Boolean {
        return crashed && crashInformation != null
    }

    fun getCrashInformation(): Throwable? {
        return crashInformation
    }

    fun setResult(result: Any?) {
        if (!running) {
            throw AsyncTaskException("Cannot set result when task isn't running")
        }
        this.result = result
    }

    fun getResult(): Any? {
        return result
    }

    class AsyncTaskException(message: String) : RuntimeException(message)
}
