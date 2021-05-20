package be.zvz.kookie.network.query

class QueryInfo {

    private var maxPlayers: Int = 0
    private var players = mapOf<Any, Any>()
    private var plugins = mapOf<Any, Any>()

    fun getPlayerCount(): Int = players.size

    fun getMaxPlayerCount(): Int = maxPlayers
}
