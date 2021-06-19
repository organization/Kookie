package be.zvz.kookie.event.world

import be.zvz.kookie.world.Position
import be.zvz.kookie.world.World

class SpawnChangeEvent(world: World, val previousSpawn: Position) : WorldEvent(world)
