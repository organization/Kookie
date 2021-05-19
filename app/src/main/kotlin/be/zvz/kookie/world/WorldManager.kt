package be.zvz.kookie.world

import be.zvz.kookie.Server
import com.koloboke.collect.map.hash.HashIntObjMaps

class WorldManager(private val server: Server) {
    val worlds: MutableMap<Int, World> = HashIntObjMaps.newMutableMap()

    fun loadWorld(world: String) {
    }

    fun unloadWorld(world: String) {
    }

    fun getWorld(id: Int): World? = worlds[id]

    fun getWorldByName(name: String): World? {
        worlds.forEach { (_, world) ->
            if (world.folderName == name) {
                return world
            }
        }
        return null
    }
}
