package be.zvz.kookie.scheduler

import com.koloboke.collect.map.hash.HashLongObjMaps
import org.slf4j.LoggerFactory
import java.util.*

class AsyncPool(private var size: Int) {
    private val logger = LoggerFactory.getLogger(AsyncPool::class.java)
    private val workers: MutableMap<Long, AsyncWorker> = HashLongObjMaps.newMutableMap()
    private val workerStartHooks = mutableListOf<(Long) -> Unit>()
    private val taskQueues: MutableMap<Long, Queue<AsyncTask>> = HashLongObjMaps.newMutableMap()

    fun getSize(): Int = size
    fun increaseSize(newSize: Int) {
        if (newSize > size) {
            size = newSize
        }
    }

    fun addWorkerStartHook(hook: (Long) -> Unit) {
        workerStartHooks.add(hook)
        workers.forEach { (key, _) ->
            hook.invoke(key)
        }
    }

    fun removeWorkerStartHook(hook: (Long) -> Unit) {
        workerStartHooks.removeIf {
            it == hook
        }
    }

    fun getRunningWorkers(): MutableSet<Long> {
        return workers.keys
    }

    private fun getWorker(worker: Long): AsyncWorker {
        if (!workers.containsKey(worker)) {
            workers[worker] = AsyncWorker(worker)
            workers[worker]?.start()
            taskQueues[worker] = LinkedList()

            workerStartHooks.forEach { hook ->
                hook.invoke(worker)
            }
        }
        return workers.getValue(worker)
    }

    fun submitTaskToWorker(task: AsyncTask, worker: Int) {
        if (worker < 0 || worker >= size) {
            throw IllegalArgumentException("Invalid worker $worker")
        }
        if (task.isSubmitted()) {
            throw IllegalArgumentException("Cannot submit the same AsyncTask instance more than once")
        }
        task.setSubmitted()
        // taskQueues[worker]
    }
}
