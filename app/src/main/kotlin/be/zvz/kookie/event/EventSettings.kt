package be.zvz.kookie.event

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class EventSettings(
    val priority: EventPriority = EventPriority.NORMAL,
    val handleCancelled: Boolean = false,
    val notHandler: Boolean = false
)
