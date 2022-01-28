package be.zvz.kookie.network.mcpe.convert

import be.zvz.kookie.network.mcpe.protocol.types.inventory.ItemStackWrapper
import be.zvz.kookie.player.GameMode
import com.nukkitx.nbt.NbtMap
import com.nukkitx.protocol.bedrock.data.GameType
import com.nukkitx.protocol.bedrock.data.inventory.ItemData

object KookieToNukkitProtocolConverter {

    @JvmStatic
    fun toItemData(itemStack: ItemStackWrapper): ItemData {
        val builder = NbtMap.builder()

        itemStack.itemStack.nbt?.value?.forEach {
            builder[it.key] = it.value
        }

        return ItemData.builder()
            .id(itemStack.itemStack.id)
            .damage(itemStack.itemStack.meta)
            .count(itemStack.itemStack.count)
            .tag(builder.build())
            .canPlace(itemStack.itemStack.canPlaceOn.toTypedArray())
            .canBreak(itemStack.itemStack.canDestroy.toTypedArray())
            .blockingTicks(-1)
            .blockRuntimeId(itemStack.itemStack.blockRuntimeId)
            .usingNetId(itemStack.stackId != 0)
            .netId(itemStack.stackId)
            .build()
    }

    fun toGameType(gameMode: GameMode): GameType {
        return when (gameMode) {
            GameMode.SURVIVAL -> GameType.SURVIVAL
            GameMode.CREATIVE -> GameType.CREATIVE
            GameMode.ADVENTURE -> GameType.ADVENTURE
            GameMode.SPECTATOR -> GameType.CREATIVE_VIEWER
        }
    }
}
