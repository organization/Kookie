package be.zvz.kookie.inventory

import be.zvz.kookie.crafting.CraftingGrid
import be.zvz.kookie.player.Player

class PlayerCraftingInventory(holder: Player) : CraftingGrid(holder, SIZE_SMALL), TemporaryInventory
