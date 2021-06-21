package be.zvz.kookie.scheduler

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

abstract class AbstractScheduler(protected val scheduler: TaskScheduler) : Scheduler() {
    override fun createWorker(): Worker = RxWorker()

    /**
     * Schedule task with the given runnable.
     *
     * @param runnable The runnable.
     * @return The task instance.
     */
    protected abstract fun schedule(runnable: Runnable): TaskHandler

    /**
     * Schedule a task with a specific delay given in minecraft server ticks.
     *
     * @param runnable The runnable.
     * @param delay    The delay.
     * @return The task instance.
     */
    protected abstract fun schedule(runnable: Runnable, delay: Int): TaskHandler

    /**
     * Schedule a task with a specific delay created by the initial delay and the following intervals.
     *
     * @param runnable The runnable.
     * @param delay    The delay.
     * @param interval The interval
     * @return The task instance.
     */
    protected abstract fun schedule(runnable: Runnable, delay: Int, interval: Int): TaskHandler

    /**
     * The worker implementation.
     */
    private inner class RxWorker : Worker() {
        /**
         * The compostie disposable for all workers.
         */
        private val compositeDisposable = CompositeDisposable()
        override fun schedule(runnable: Runnable, delay: Long, unit: TimeUnit): Disposable {
            val bukkitTask = this@AbstractScheduler.schedule(runnable, convertTimeToTicks(delay, unit))
            val disposable: Disposable = DisposableBukkitTask(bukkitTask)
            compositeDisposable.add(disposable)
            return disposable
        }

        override fun schedulePeriodically(runnable: Runnable, initialDelay: Long, period: Long, unit: TimeUnit): Disposable {
            val bukkitTask = this@AbstractScheduler.schedule(
                runnable,
                convertTimeToTicks(initialDelay, unit),
                convertTimeToTicks(period, unit)
            )
            val disposable: Disposable = DisposableBukkitTask(bukkitTask)
            compositeDisposable.add(disposable)
            return disposable
        }

        override fun schedule(runnable: Runnable): Disposable {
            val bukkitTask = this@AbstractScheduler.schedule(runnable)
            val disposable: Disposable = DisposableBukkitTask(bukkitTask)
            compositeDisposable.add(disposable)
            return disposable
        }

        /**
         * Convert java time to ticks.
         *
         * @param time     The time.
         * @param timeUnit The time unit.
         * @return The time in ticks.
         */
        private fun convertTimeToTicks(time: Long, timeUnit: TimeUnit): Int =
            (timeUnit.toMillis(time).toDouble() / 50.0).roundToInt()

        override fun dispose() {
            compositeDisposable.dispose()
        }

        override fun isDisposed(): Boolean {
            return compositeDisposable.isDisposed
        }

        /**
         * Wrap a task in a disposable.
         *
         * @param task The task we want to dispose.
         */
        private inner class DisposableBukkitTask(private val task: TaskHandler) : Disposable {
            /**
             * If the task is disposed.
             */
            private var disposed = false
            override fun dispose() {
                disposed = true
                task.cancel()
            }

            override fun isDisposed(): Boolean {
                return disposed && task.isCancelled
            }
        }
    }
}
