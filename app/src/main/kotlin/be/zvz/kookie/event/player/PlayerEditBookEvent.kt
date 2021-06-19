package be.zvz.kookie.event.player

import be.zvz.kookie.event.Cancellable
import be.zvz.kookie.player.Player

// TODO: Implement this when WritableBook is exist
class PlayerEditBookEvent(player: Player) : PlayerEvent(player), Cancellable {
    override var isCancelled: Boolean = false

    enum class Action(action: Int) {
        REPLACE_PAGE(0),
        ADD_PAGE(1),
        DELETE_PAGE(2),
        SWAP_PAGE(3),
        SIGN_BOOK(4)
    }
}
