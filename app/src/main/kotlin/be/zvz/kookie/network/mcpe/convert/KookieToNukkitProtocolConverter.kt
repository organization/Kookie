/**
 *
 * _  __           _    _
 * | |/ /___   ___ | | _(_) ___
 * | ' // _ \ / _ \| |/ / |/ _ \
 * | . \ (_) | (_) |   <| |  __/
 * |_|\_\___/ \___/|_|\_\_|\___|
 *
 * A server software for Minecraft: Bedrock Edition
 *
 * Copyright (C) 2021 - 2022 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.network.mcpe.convert

import be.zvz.kookie.network.mcpe.protocol.types.inventory.ItemStackWrapper
import be.zvz.kookie.player.GameMode
import com.nukkitx.nbt.NbtMap
import com.nukkitx.protocol.bedrock.data.GameType
import com.nukkitx.protocol.bedrock.data.entity.EntityData
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

    @JvmStatic
    fun toGameType(gameMode: GameMode): GameType {
        return when (gameMode) {
            GameMode.SURVIVAL -> GameType.SURVIVAL
            GameMode.CREATIVE -> GameType.CREATIVE
            GameMode.ADVENTURE -> GameType.ADVENTURE
            GameMode.SPECTATOR -> GameType.CREATIVE_VIEWER
        }
    }

    @JvmStatic
    fun toEntityData(id: Int, value: Any): EntityData {
        val constructor = EntityData::class.java.getDeclaredConstructor(Int::class.java)
        return constructor.newInstance(id, value)
        //     return when (id) {
        //         // EntityMetadataProperties.FLAGS -> EntityData.FLAGS
        //         // EntityMetadataProperties.HEALTH -> EntityData.HEALTH
        //         // EntityMetadataProperties.VARIANT -> EntityData.VARIANT
        //         // EntityMetadataProperties.COLOR -> EntityData.COLOR
        //         // EntityMetadataProperties.NAMETAG -> EntityData.NAMETAG
        //         // EntityMetadataProperties.OWNER_EID -> EntityData.OWNER_EID
        //         // EntityMetadataProperties.TARGET_EID -> EntityData.TARGET_EID
        //         // EntityMetadataProperties.AIR -> EntityData.AIR_SUPPLY
        //         // EntityMetadataProperties.POTION_COLOR -> EntityData.EFFECT_COLOR
        //         // EntityMetadataProperties.POTION_AMBIENT -> EntityData.EFFECT_AMBIENT
        //         // EntityMetadataProperties.HURT_TIME -> EntityData.HURT_TIME
        //         // EntityMetadataProperties.HURT_DIRECTION -> EntityData.HURT_DIRECTION
        //         // EntityMetadataProperties.PADDLE_TIME_LEFT -> EntityData.ROW_TIME_LEFT
        //         // EntityMetadataProperties.PADDLE_TIME_RIGHT -> EntityData.ROW_TIME_RIGHT
        //         // EntityMetadataProperties.EXPERIENCE_VALUE -> EntityData.EXPERIENCE_VALUE
        //         // EntityMetadataProperties.MINECART_DISPLAY_BLOCK -> EntityData.CARRIED_BLOCK
        //         // EntityMetadataProperties.HORSE_FLAGS -> EntityData.CAN_RIDE_TARGET
        //         // EntityMetadataProperties.MINECART_DISPLAY_OFFSET -> EntityData.DISPLAY_OFFSET
        //         // EntityMetadataProperties.SHOOTER_ID -> EntityData.OWNER_EID
        //         //
        //         //
        //         else -> throw AssertionError("Unknown entity data id: $id")
        //     }
        // }
    }
}
