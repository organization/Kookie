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
package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.EVENT_PACKET)
class EventPacket : DataPacket(), ClientboundPacket {

    var playerRuntimeId: Long = 0
    var eventData: Int = 0
    var type: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        playerRuntimeId = input.getEntityRuntimeId()
        eventData = input.getVarInt()
        type = input.getByte()

        // TODO: nice confusing mess
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityRuntimeId(playerRuntimeId)
        output.putVarInt(eventData)
        output.putByte(type)

        // TODO: also nice confusing mess
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleEvent(this)

    enum class TYPE(value: Int) {
        ACHIEVEMENT_AWARDED(0),
        ENTITY_INTERACT(1),
        PORTAL_BUILT(2),
        PORTAL_USED(3),
        MOB_KILLED(4),
        CAULDRON_USED(5),
        PLAYER_DEATH(6),
        BOSS_KILLED(7),
        AGENT_COMMAND(8),
        AGENT_CREATED(9),
        PATTERN_REMOVED(10), // ???
        COMMAND_EXECUTED(11),
        FISH_BUCKETED(12),
        MOB_BORN(13),
        PET_DIED(14),
        CAULDRON_BLOCK_USED(15),
        COMPOSTER_BLOCK_USED(16),
        BELL_BLOCK_USED(17),
        ACTOR_DEFINITION(18),
        RAID_UPDATE(19),
        PLAYER_MOVEMENT_ANOMALY(20), // anti cheat
        PLAYER_MOVEMENT_CORRECTED(21),
        HONEY_HARVESTED(22),
        TARGET_BLOCK_HIT(23),
        PIGLIN_BARTER(24),
    }
}
