package be.zvz.kookie.event.world

import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.world.World

class WorldUnloadEvent(world: World) : WorldEvent(world), Cancellable {
    override var isCancelled: Boolean = false
}
