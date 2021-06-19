package be.zvz.kookie.event.player

import be.zvz.kookie.entity.Human
import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.event.entity.EntityEvent

class PlayerExhaustEvent(human: Human, var amount: Float, var cause: Type) : EntityEvent(human), Cancellable {
    override var isCancelled: Boolean = false

    enum class Type(cause: Int) {
        ATTACK(1),
        DAMAGE(2),
        MINING(3),
        HEALTH_REGEN(4),
        POTION(5),
        WALKING(6),
        SPRINTING(7),
        SWIMMING(8),
        JUMPING(9),
        SPRINT_JUMPING(10),
        CUSTOM(11)
    }
}
