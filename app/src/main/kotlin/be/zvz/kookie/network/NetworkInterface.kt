package be.zvz.kookie.network

interface NetworkInterface {

    fun start()

    fun setName(name: String)

    fun tick()

    fun shutdown()
}
