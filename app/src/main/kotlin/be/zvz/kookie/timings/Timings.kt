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
package be.zvz.kookie.timings

import be.zvz.kookie.block.tile.Tile
import be.zvz.kookie.entity.Entity
import be.zvz.kookie.player.Player
import be.zvz.kookie.scheduler.TaskHandler
import com.koloboke.collect.map.hash.HashObjObjMaps
import com.nukkitx.protocol.bedrock.BedrockPacket

object Timings {
    const val INCLUDED_BY_OTHER_TIMINGS_PREFIX = "** "

    private val timings: MutableMap<String, TimingsHandler> = HashObjObjMaps.newMutableMap()

    val fullTick = TimingsHandler("Full Server Tick")
    val serverTick = TimingsHandler(INCLUDED_BY_OTHER_TIMINGS_PREFIX + "Full Server Tick", fullTick)
    val memoryManager = TimingsHandler("Memory Manager")
    val garbageCollector = TimingsHandler("Garbage Collector", memoryManager)
    val titleTick = TimingsHandler("Console Title Tick")

    val playerNetworkSend = TimingsHandler("Player Network Send")
    val playerNetworkSendCompress =
        TimingsHandler(INCLUDED_BY_OTHER_TIMINGS_PREFIX + "Player Network Send - Compression", playerNetworkSend)
    val playerNetworkSendEncrypt =
        TimingsHandler(INCLUDED_BY_OTHER_TIMINGS_PREFIX + "Player Network Send - Encryption", playerNetworkSend)

    val playerNetworkReceive = TimingsHandler("Player Network Receive")
    val playerNetworkReceiveDecompress =
        TimingsHandler(INCLUDED_BY_OTHER_TIMINGS_PREFIX + "Player Network Receive - Decompression", playerNetworkReceive)
    val playerNetworkReceiveDecrypt =
        TimingsHandler(INCLUDED_BY_OTHER_TIMINGS_PREFIX + "Player Network Receive - Decryption", playerNetworkReceive)

    val broadcastPackets = TimingsHandler(INCLUDED_BY_OTHER_TIMINGS_PREFIX + "Broadcast Packets", playerNetworkSend)

    val playerChunkOrder = TimingsHandler("Player Order Chunks")
    val playerChunkSend = TimingsHandler("Player Send Chunks")

    val connection = TimingsHandler("Connection Handler")
    val scheduler = TimingsHandler("Scheduler")
    val worldLoad = TimingsHandler("World Load")
    val worldSave = TimingsHandler("World Save")
    val population = TimingsHandler("World Population")
    val generation = TimingsHandler("World Generation")
    val generationCallback = TimingsHandler("World Generation Callback")
    val permissibleCalculation = TimingsHandler("Permissible Calculation")
    val permissibleCalculationDiff =
        TimingsHandler(INCLUDED_BY_OTHER_TIMINGS_PREFIX + "Permission Calculation - Diff", permissibleCalculation)
    val permissibleCalculationCallback =
        TimingsHandler(INCLUDED_BY_OTHER_TIMINGS_PREFIX + "Permission Calculation - Callbacks", permissibleCalculation)

    val syncPlayerDataLoad = TimingsHandler("Player Data Load")
    val syncPlayerDataSave = TimingsHandler("Player Data Save")

    val entityMove = TimingsHandler(INCLUDED_BY_OTHER_TIMINGS_PREFIX + "entityMove")
    val playerCHeckNearEntities = TimingsHandler(INCLUDED_BY_OTHER_TIMINGS_PREFIX + "checkNearEntities")
    val tickEntity = TimingsHandler(INCLUDED_BY_OTHER_TIMINGS_PREFIX + "tickEntity")
    val tickTileEntity = TimingsHandler(INCLUDED_BY_OTHER_TIMINGS_PREFIX + "tickTileEntity")

    val entityBaseTick = TimingsHandler(INCLUDED_BY_OTHER_TIMINGS_PREFIX + "entityBaseTick")
    val livingEntityBaseTick = TimingsHandler(INCLUDED_BY_OTHER_TIMINGS_PREFIX + "livingEntityBaseTick")

    var schedulerSync = TimingsHandler(INCLUDED_BY_OTHER_TIMINGS_PREFIX + "Scheduler - Sync Tasks")
    val schedulerAsync = TimingsHandler(INCLUDED_BY_OTHER_TIMINGS_PREFIX + "Scheduler - Async Tasks")

    val playerCommand = TimingsHandler(INCLUDED_BY_OTHER_TIMINGS_PREFIX + "playerCommand")
    val craftingDataCacheRebuild = TimingsHandler(INCLUDED_BY_OTHER_TIMINGS_PREFIX + "craftingDataCacheRebuild")
    
    private val entityTimings: MutableMap<String, TimingsHandler> = HashObjObjMaps.newMutableMap()
    private val pluginTaskTimings: MutableMap<String, TimingsHandler> = HashObjObjMaps.newMutableMap()

    @JvmStatic
    fun getTileEntityTimings(tile: Tile): TimingsHandler {
        var exist = timings[tile::class.java.simpleName]
        if (exist == null) {
            exist = TimingsHandler("Tile ${tile::class.java.simpleName}")
            timings[tile::class.java.simpleName] = exist
        }
        return exist
    }

    @JvmStatic
    fun getPacketDecodeTimings(packet: BedrockPacket): TimingsHandler {
        val networkId = packet.packetId
        val key = "PacketDecode: $networkId"
        var exist = timings[key]
        if (exist === null) {
            exist = TimingsHandler("Packet ${packet::class.java.simpleName}($networkId) decode", null)
            timings[key] = exist
        }
        return exist
    }

    @JvmStatic
    fun getPacketSendTimings(packet: BedrockPacket): TimingsHandler {
        val networkId = packet.packetId
        val key = "PacketSend: $networkId"
        var exist = timings[key]
        if (exist === null) {
            exist = TimingsHandler("Packet ${packet::class.java.simpleName}($networkId) send", null)
            timings[key] = exist
        }
        return exist
    }

    fun getScheduledTaskTimings(task: TaskHandler, period: Int): TimingsHandler {
        val name = "Task: ${task.ownerName} Runnable: ${task.taskName}" + if (period > 0) {
            "(interval:$period)"
        } else {
            "(Single)"
        }

        if (!pluginTaskTimings.containsKey(name)) {
            pluginTaskTimings[name] = TimingsHandler(name, schedulerSync)
        }

        return pluginTaskTimings.getValue(name)
    }

    fun getEntityTimings(entity: Entity): TimingsHandler {
        val entityType = entity::class.java.simpleName
        if (!entityTimings.containsKey(entityType)) {
            if (entity is Player) {
                entityTimings[entityType] = TimingsHandler(
                    INCLUDED_BY_OTHER_TIMINGS_PREFIX + "tickEntity - EntityPlayer", tickEntity
                )
            } else {
                entityTimings[entityType] = TimingsHandler(
                    INCLUDED_BY_OTHER_TIMINGS_PREFIX + "tickEntity - $entityType", tickEntity
                )
            }
        }
        return entityTimings.getValue(entityType)
    }
}
