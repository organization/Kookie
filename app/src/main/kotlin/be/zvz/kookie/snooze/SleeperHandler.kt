package be.zvz.kookie.snooze

import com.koloboke.collect.map.hash.HashIntIntMaps
import com.koloboke.collect.map.hash.HashIntObjMaps
import java.util.concurrent.TimeUnit

class SleeperHandler {
    private val sharedObject = SharedObject()
    private val notifiers = HashIntObjMaps.newMutableMap<() -> Unit>()
    private var nextSleeperId = 0

    fun addNotifier(notifier: SleeperNotifier, handler: () -> Unit) {
        val id = nextSleeperId++
        notifier.attachSleeper(sharedObject, id)
        notifiers[id] = handler
    }

    fun removeHandler(notifier: SleeperNotifier) {
        notifiers.remove(notifier.sleeperId)
    }

    fun sleep(timeout: Long) {
        try {
            if (sharedObject.lock.tryLock(timeout, TimeUnit.MILLISECONDS)) {
                if (sharedObject.ids.isEmpty()) {
                    (sharedObject as Object).wait(timeout)
                }
            }
        } finally {
            sharedObject.lock.unlock()
        }
    }

    fun sleepUntil(unixTime: Long) {
        while (true) {
            processNotifications()

            val sleepTime = (unixTime - System.currentTimeMillis()) * 1000
            if (sleepTime > 0) {
                sleep(sleepTime)
            } else {
                break
            }
        }
    }

    fun sleepUntilNotification() {
        sleep(0)
        processNotifications()
    }

    fun processNotifications() {
        while (true) {
            val notifierIds = try {
                val notifierIds: MutableMap<Int, Int> = HashIntIntMaps.newMutableMap()
                if (sharedObject.lock.tryLock()) {
                    sharedObject.ids.forEach { (key, _) ->
                        notifierIds[key] = key
                        sharedObject.ids.remove(key)
                    }
                }
                notifierIds
            } finally {
                sharedObject.lock.unlock()
            }
            if (notifierIds.isEmpty()) {
                break
            }
            notifierIds.forEach { (key, _) ->
                if (!notifiers.containsKey(key)) {
                    return@forEach
                }
                notifiers[key]?.invoke()
            }
        }
    }
}
