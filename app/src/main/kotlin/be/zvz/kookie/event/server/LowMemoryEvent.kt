package be.zvz.kookie.event.server

class LowMemoryEvent(val memory: Int, val memoryLimit: Int, val triggerCount: Int = 0) {
    fun getMemoryFreed(): Long {
        val runtime = Runtime.getRuntime()
        return memory - runtime.totalMemory()
    }
}
