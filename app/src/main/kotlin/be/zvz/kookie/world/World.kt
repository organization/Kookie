package be.zvz.kookie.world

import be.zvz.kookie.Server

class World(val server: Server, val folderName: String) {
    companion object {
        const val DIFFICULTY_PEACEFUL = 0
        const val DIFFICULTY_EASY = 1
        const val DIFFICULTY_NORMAL = 2
        const val DIFFICULTY_HARD = 3
    }
}
