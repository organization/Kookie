package be.zvz.kookie.block.util

import be.zvz.kookie.block.Block

interface Fallable {
    /**
     * Called every tick by FallingBlock to update the falling state of this block. Used by concrete to check when it
     * hits water.
     * Return null if you don't want to change the usual behaviour.
     */
    fun tickFalling(): Block?
}
