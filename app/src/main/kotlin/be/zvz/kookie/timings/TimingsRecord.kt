package be.zvz.kookie.timings

import java.util.*
import kotlin.math.roundToInt

class TimingsRecord(val handler: TimingsHandler) {

    var count: Long = 0
        private set
    var curCount: Long = 0
        private set
    var start: Long = 0
        private set
    var totalTime: Long = 0
        private set
    var curTickTotal: Long = 0
        private set
    var violations: Int = 0
        private set

    init {
        records.add(this)
    }

    fun getName(): String = handler.name

    fun startTiming(now: Long) {
        start = now
    }

    fun stopTiming(now: Long) {
        if (start == 0L) {
            return
        }
        val diff = now - start
        totalTime += diff
        curTickTotal += diff
        ++curCount
        ++count
        start = 0
    }

    companion object {
        private val records: MutableList<TimingsRecord> = mutableListOf()

        fun clearRecords() {
            records.clear()
        }

        fun getAll(): MutableList<TimingsRecord> = records

        fun tick(measure: Boolean = true) {
            if (measure) {
                records.forEach {
                    if (it.curTickTotal > 50000000L) {
                        it.violations += (it.curTickTotal / 50000000).toDouble().roundToInt()
                    }
                    it.curTickTotal = 0
                    it.curCount = 0
                }
            } else {
                records.forEach {
                    it.totalTime -= it.curTickTotal
                    it.count -= it.curCount
                    it.curTickTotal = 0
                    it.curCount = 0
                }
            }
        }
    }
}
