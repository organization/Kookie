package be.zvz.kookie.scheduler

import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class AsyncPool(
    maxPoolSize: Int,
) {
    private val scheduledThreadPool = Executors.newScheduledThreadPool(maxPoolSize)
    fun <T> submit(task: AsyncTask<T>): Future<T>? {
        if (task.isSubmitted()) {
            throw IllegalArgumentException("Cannot submit the same AsyncTask instance more than once")
        }
        task.setSubmitted()
        return scheduledThreadPool.submit(task)
    }

    fun <T> schedule(id: Long, task: AsyncTask<T>, delay: Long, timeunit: TimeUnit): ScheduledFuture<T>? {
        if (task.isSubmitted()) {
            throw IllegalArgumentException("Cannot schedule the same AsyncTask instance more than once")
        }
        task.setSubmitted()
        return scheduledThreadPool.schedule(task, delay, timeunit)
    }
}
