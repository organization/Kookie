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
 * Copyright (C) 2021 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.network.mcpe.handler

import be.zvz.kookie.Server
import be.zvz.kookie.network.mcpe.InventoryManager
import be.zvz.kookie.network.mcpe.NetworkSession
import be.zvz.kookie.player.Player
import com.nukkitx.protocol.bedrock.packet.AvailableEntityIdentifiersPacket
import com.nukkitx.protocol.bedrock.packet.BiomeDefinitionListPacket

class PreSpawnPacketHandler(
    val server: Server,
    val player: Player,
    val session: NetworkSession,
    val inventoryManager: InventoryManager
) : PacketHandler() {

    init {
        val location = player.location

        /* TODO:
        session.sendDataPacket(
            StartGamePacket().apply {
                runtimeEntityId = player.getId()
                uniqueEntityId = player.getId()
                seed = -1
                spawnBiomeType = SpawnBiomeType.DEFAULT
                customBiomeName = ""
                dimensionId = DimensionIds.OVERWORLD.id
                levelGameType = KookieToNukkitProtocolConverter.toGameType(server.gamemode)
                difficulty = location.world!!.difficulty
                defaultSpawn = Vector3i.ZERO.add(location.floorX, location.floorY, location.floorZ)
                isAchievementsDisabled = true
                dayCycleStopTime = location.world!!.time.toInt()
                eduEditionOffers = 0
                rainLevel = 0f
                lightningLevel = 0f
                isCommandsEnabled = true
                gamerules.add(GameRuleData("naturalregeneration", false, false)) // Hack for client side regeneration
                experiments.add(ExperimentData("data_driven_items", true))
                playerPosition = Vector3f.ZERO.add(location.x, location.y, location.z)
                playerGameType = KookieToNukkitProtocolConverter.toGameType(server.gamemode)
            }
        )
         */

        session.sendDataPacket(AvailableEntityIdentifiersPacket()) // TODO: Available entity identifiers
        session.sendDataPacket(BiomeDefinitionListPacket()) // TODO: Biome definitions
        session.syncAttributes(player, player.attributeMap.getAll())
        // TODO: session.syncAvailableCommands()
        player.effectManager.all().forEach { (_, effect) ->
            // TODO: session.onEntityEffectAdded(player, effect, false)
        }

        player.sendData(mutableMapOf(player.getId() to player))

        inventoryManager.syncAll()
        inventoryManager.syncCreative()
        inventoryManager.syncSelectedHotbarSlot()
        // TODO: session.sendDataPacket(CraftingDataCache.getCache(server.craftingManager))

        // TODO: session.syncPlayerList(server.onlinePlayers)
    }
}
