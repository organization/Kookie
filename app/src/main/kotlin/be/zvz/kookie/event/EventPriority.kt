package be.zvz.kookie.event

enum class EventPriority(val priority: Int) {
    /**
     * Event call is of very low importance and should be ran first, to allow
     * other plugins to further customise the outcome
     */
    LOWEST(5),

    /**
     * Event call is of low importance
     */
    LOW(4),

    /**
     * Event call is neither important or unimportant, and may be ran normally.
     * This is the default priority.
     */
    NORMAL(3),

    /**
     * Event call is of high importance
     */
    HIGH(2),

    /**
     * Event call is critical and must have the final say in what happens
     * to the event
     */
    HIGHEST(1),

    /**
     * Event is listened to purely for monitoring the outcome of an event.
     *
     * No modifications to the event should be made under this priority
     */
    MONITOR(0);

    companion object {
        private val VALUES = values()

        @JvmStatic
        fun from(value: Int): EventPriority? = VALUES.firstOrNull { it.priority == value }
        fun from(value: String): EventPriority? = when (value.uppercase()) {
            "LOWEST" -> LOWEST
            "LOW" -> LOW
            "NORMAL" -> NORMAL
            "HIGH" -> HIGH
            "HIGHEST" -> HIGHEST
            "MONITOR" -> MONITOR
            else -> null
        }

        val ALL: List<EventPriority> = listOf(
            LOWEST,
            LOW,
            NORMAL,
            HIGH,
            HIGHEST,
            MONITOR
        )
    }
}
