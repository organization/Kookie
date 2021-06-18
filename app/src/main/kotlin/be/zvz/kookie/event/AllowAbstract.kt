package be.zvz.kookie.event

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class AllowAbstract(
    val allowed: Boolean
)
