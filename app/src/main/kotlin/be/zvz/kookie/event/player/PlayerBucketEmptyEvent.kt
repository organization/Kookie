package be.zvz.kookie.event.player

import be.zvz.kookie.block.Block
import be.zvz.kookie.item.Item
import be.zvz.kookie.player.Player

class PlayerBucketEmptyEvent(
    player: Player,
    blockClicked: Block,
    blockFace: Int,
    bucket: Item,
    itemInHand: Item
) : PlayerBucketEvent(
    player,
    blockClicked,
    blockFace, bucket, itemInHand
)
