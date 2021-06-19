package be.zvz.kookie.event.entity

import be.zvz.kookie.entity.Entity

class EntityRegainHealthEvent(entity: Entity, var amount: Float, var regainReason: Type) : EntityEvent(entity) {

    enum class Type(cause: Int) {
        REGEN(0),
        EATING(1),
        MAGIC(2),
        CUSTOM(3),
        SATURATION(4)
    }
}
