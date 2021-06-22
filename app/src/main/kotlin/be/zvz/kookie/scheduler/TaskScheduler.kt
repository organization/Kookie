package be.zvz.kookie.scheduler

import com.koloboke.collect.set.hash.HashObjSets
import java.util.PriorityQueue

open class TaskScheduler(
    private val owner: String? = null
) {
    private var enabled = true
    protected val queue: PriorityQueue<Pair<Long, TaskHandler>> = PriorityQueue { first, second ->
        -(first.first - second.first).toInt()
    }
    protected val tasks = HashObjSets.newMutableSet<TaskHandler>()
    protected var currentTick = 0L

    fun scheduleTask(task: Task): TaskHandler {
        return addTask(task, -1, -1)
    }

    fun scheduleDelayedTask(task: Task, delay: Int): TaskHandler {
        return addTask(task, delay, -1)
    }

    fun scheduleRepeatingTask(task: Task, period: Int): TaskHandler {
        return addTask(task, -1, period)
    }

    fun scheduleTask(task: Task, delay: Int, period: Int): TaskHandler {
        return addTask(task, delay, period)
    }

    private fun addTask(task: Task, delay: Int, period: Int): TaskHandler {
        if (!enabled) {
            throw IllegalStateException("Tried to schedule task to disabled scheduler")
        }

        return handle(TaskHandler(task, delay, period, owner))
    }

    fun isQueued(handler: TaskHandler): Boolean = queue.contains(Pair(handler.nextRun, handler))

    open fun cancelAllTasks() {
        tasks.apply {
            val iterator = iterator()
            while (iterator.hasNext()) {
                val current = iterator.next()
                current.cancel()
                iterator.remove()
            }
        }
    }

    fun shutdown() {
        enabled = false
        cancelAllTasks()
    }

    fun mainThreadHeartbeat(currentTick: Long) {
        this.currentTick = currentTick
        val iterator = queue.iterator()
        while (iterator.hasNext()) {
            val task = iterator.next()
            if (task.second.nextRun <= currentTick) {
                if (task.second.isCancelled) {
                    tasks.remove(task.second)
                    iterator.remove()
                    continue
                }
                task.second.run()
                if (!task.second.isCancelled && task.second.period > 0) {
                    task.second.nextRun = currentTick + task.second.period
                    queue.add(Pair(currentTick + task.second.period, task.second))
                    iterator.remove()
                } else {
                    task.second.remove()
                    iterator.remove()
                }
            }
        }
    }

    private fun handle(handler: TaskHandler): TaskHandler {
        val nextRun = if (handler.isDelayed()) {
            currentTick + handler.delay
        } else {
            currentTick
        }

        handler.nextRun = nextRun
        tasks.add(handler)
        queue.add(Pair(nextRun, handler))
        return handler
    }
}
