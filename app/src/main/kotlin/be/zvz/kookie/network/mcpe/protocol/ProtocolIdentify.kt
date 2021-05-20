package be.zvz.kookie.network.mcpe.protocol

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ProtocolIdentify(val networkId: Int)
