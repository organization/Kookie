package be.zvz.kookie.item

import be.zvz.kookie.entity.Consumable

interface ConsumableItem : Consumable, Releasable {

    fun getResidue(): Item
}
