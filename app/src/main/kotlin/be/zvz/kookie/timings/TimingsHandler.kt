package be.zvz.kookie.timings

import java.util.*

class TimingsHandler(val name: String, private val parent: TimingsHandler? = null) {
    var record: TimingsRecord? = null
    var timingsDepth = 0

    fun startTiming() {
        if (enabled) {
            internalStartTiming(Date().time)
        }
    }

    private fun internalStartTiming(now: Long) {
        if (++timingsDepth == 1) {
            record?.startTiming(now) ?: run {
                record = TimingsRecord(this).apply {
                    startTiming(now)
                }
            }
            parent?.internalStartTiming(now)
        }
    }

    fun stopTiming() {
        if (enabled) {
            internalStopTiming(Date().time)
        }
    }

    private fun internalStopTiming(now: Long) {
        if (timingsDepth == 0) {
            return
        }
        if (--timingsDepth != 0) {
            return
        }
        record?.stopTiming(now)
        parent?.internalStopTiming(now)
    }

    fun time(callback: () -> Any): Any {
        startTiming()
        try {
            return callback()
        } finally {
            stopTiming()
        }
    }

    fun destroyCycles() {
        record = null
    }

    companion object {
        var enabled: Boolean = false
        var timingStart: Long = 0

        fun tick(measure: Boolean) {
            TimingsRecord.tick(measure)
        }
    }
}
