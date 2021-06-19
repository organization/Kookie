package be.zvz.kookie.event.player

import be.zvz.kookie.entity.Human
import be.zvz.kookie.event.entity.EntityEvent

class PlayerExperienceChangeEvent(
    human: Human,
    val oldLevel: Int,
    val oldProgress: Float,
    var newLevel: Int?,
    var newProgress: Int?
) : EntityEvent(human)
