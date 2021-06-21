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
import be.zvz.kookie.network.mcpe.protocol.DataPacket
import be.zvz.kookie.network.mcpe.protocol.ProtocolIdentify
import be.zvz.kookie.network.mcpe.protocol.ProtocolInfo
import be.zvz.kookie.player.Player
import be.zvz.kookie.scheduler.TaskHandler
import com.koloboke.collect.map.hash.HashObjObjMaps

object Timings {
    const val INCLUDED_BY_OTHER_TIMINGS_PREFIX = "** "

    private val timings: MutableMap<String, TimingsHandler> = HashObjObjMaps.newMutableMap()

    private val entityTimings: MutableMap<String, TimingsHandler> = HashObjObjMaps.newMutableMap()
    private val pluginTaskTimings: MutableMap<String, TimingsHandler> = HashObjObjMaps.newMutableMap()

    var schedulerSync = TimingsHandler(INCLUDED_BY_OTHER_TIMINGS_PREFIX + "Scheduler - Sync Tasks")
    var tickEntity = TimingsHandler(INCLUDED_BY_OTHER_TIMINGS_PREFIX + "tickEntity")

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
    fun getPacketDecodeTimings(packet: DataPacket): TimingsHandler {
        val protocolIdentify = this::class.java.getAnnotation(ProtocolIdentify::class.java)!!
        val networkId = if (protocolIdentify.networkId == ProtocolInfo.IDS.UNKNOWN && protocolIdentify.customId != -1) {
            protocolIdentify.customId
        } else {
            protocolIdentify.networkId.id
        }
        val key = "PacketDecode: $networkId"
        var exist = timings[key]
        if (exist === null) {
            exist = TimingsHandler("Packet ${packet::class.java.simpleName}($networkId) decode", null)
            timings[key] = exist
        }
        return exist
    }

    @JvmStatic
    fun getPacketSendTimings(packet: DataPacket): TimingsHandler {
        val protocolIdentify = this::class.java.getAnnotation(ProtocolIdentify::class.java)!!
        val networkId = if (protocolIdentify.networkId == ProtocolInfo.IDS.UNKNOWN && protocolIdentify.customId != -1) {
            protocolIdentify.customId
        } else {
            protocolIdentify.networkId.id
        }
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
