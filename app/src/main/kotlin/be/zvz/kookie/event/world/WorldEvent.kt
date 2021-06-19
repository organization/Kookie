package be.zvz.kookie.event.world

import be.zvz.kookie.event.Event
import be.zvz.kookie.world.World

abstract class WorldEvent(val world: World) : Event()
