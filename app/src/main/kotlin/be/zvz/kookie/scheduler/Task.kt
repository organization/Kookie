package be.zvz.kookie.scheduler

abstract class Task {
    open val name: String
        get() = this::class.java.simpleName

    var handler: TaskHandler? = null
        set(value) {
            if (field === null || value === null) {
                field = value
            }
        }

    @Throws(CancelTaskException::class)
    abstract fun onRun()

    open fun onCancel() {}
}
