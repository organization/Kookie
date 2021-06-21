package be.zvz.kookie.scheduler

import be.zvz.kookie.timings.Timings
import be.zvz.kookie.timings.TimingsHandler

open class TaskHandler @JvmOverloads constructor(
    task: Task,
    delay: Int = -1,
    period: Int = -1,
    ownerName: String? = null
) {
    var task = task
        protected set
    var delay = delay
        protected set
    var period = period
        protected set
    var nextRun = 0L
    var isCancelled = false
        protected set
    val taskName: String = task.name
    val ownerName = ownerName ?: "Unknown"
    private val timings: TimingsHandler by lazy {
        Timings.getScheduledTaskTimings(this, period)
    }

    open fun cancel() {
        try {
            task.onCancel()
        } finally {
            remove()
        }
    }

    open fun remove() {
        isCancelled = true
        task.handler = null
    }

    open fun run() {
        timings.startTiming()
        try {
            task.onRun()
        } catch (_: CancelTaskException) {
            cancel()
        } finally {
            timings.stopTiming()
        }
    }

    open fun isDelayed(): Boolean = delay > 0
}
