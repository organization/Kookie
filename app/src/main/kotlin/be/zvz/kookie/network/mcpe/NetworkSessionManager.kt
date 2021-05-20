package be.zvz.kookie.network.mcpe

class NetworkSessionManager {

    private val sessions = mutableListOf<NetworkSession>()

    private val updateSessions = mutableListOf<NetworkSession>()

    fun add(session: NetworkSession) {
        sessions.add(session)
    }

    fun remove(session: NetworkSession) {
        sessions.remove(session)
    }

    fun scheduleUpdate(session: NetworkSession) {
        updateSessions.add(session)
    }

    fun getSessionsCount(): Int {
        return sessions.size
    }

    fun tick() {
        updateSessions.forEach {
            if (!it.tick()) {
                updateSessions.remove(it)
            }
        }
    }

    fun close(reason: String) {
        sessions.forEach {
            it.disconnect(reason)
        }
        sessions.clear()
        updateSessions.clear()
    }
}
