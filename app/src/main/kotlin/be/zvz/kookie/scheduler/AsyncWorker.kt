package be.zvz.kookie.scheduler

class AsyncWorker(private val id: Long) : Thread() {
    override fun getId(): Long {
        return id
    }
}
