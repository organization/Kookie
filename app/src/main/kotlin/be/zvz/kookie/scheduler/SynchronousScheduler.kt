package be.zvz.kookie.scheduler

class SynchronousScheduler(scheduler: TaskScheduler) : AbstractScheduler(scheduler) {
    override fun schedule(runnable: Runnable): TaskHandler {
        return scheduler.scheduleTask(object : Task() {
            override fun onRun() {
                runnable.run()
            }
        })
    }

    override fun schedule(runnable: Runnable, delay: Int): TaskHandler {
        return scheduler.scheduleDelayedTask(
            object : Task() {
                override fun onRun() {
                    runnable.run()
                }
            },
            delay
        )
    }

    override fun schedule(runnable: Runnable, delay: Int, interval: Int): TaskHandler {
        return scheduler.scheduleTask(
            object : Task() {
                override fun onRun() {
                    runnable.run()
                }
            },
            delay, interval
        )
    }
}
