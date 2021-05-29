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
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.BOSS_EVENT_PACKET)
class BossEventPacket : DataPacket(), ClientboundPacket, ServerboundPacket {
    var bossEid: Long = 0
    var eventType: Int = 0

    var playerEid: Long = 0
    var healthPercent: Float = 0f
    lateinit var title: String
    var unknownShort: Int = 0
    var color: Int = 0
    var overlay: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        bossEid = input.getEntityUniqueId()
        eventType = input.getUnsignedVarInt()

        when (eventType) {
            TYPE_REGISTER_PLAYER, TYPE_UNREGISTER_PLAYER -> playerEid = input.getEntityUniqueId()
            TYPE_SHOW -> {
                title = input.getString()
                healthPercent = input.getLFloat()
                unknownShort = input.getLShort()
                color = input.getUnsignedVarInt()
                overlay = input.getUnsignedVarInt()
            }
            TYPE_UNKNOWN_6 -> {
                unknownShort = input.getLShort()
                color = input.getUnsignedVarInt()
                overlay = input.getUnsignedVarInt()
            }
            TYPE_TEXTURE -> {
                color = input.getUnsignedVarInt()
                overlay = input.getUnsignedVarInt()
            }
            TYPE_HEALTH_PERCENT -> healthPercent = input.getLFloat()
            TYPE_TITLE -> title = input.getString()
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putEntityUniqueId(bossEid)
        output.putUnsignedVarInt(eventType)

        when (eventType) {
            TYPE_REGISTER_PLAYER, TYPE_UNREGISTER_PLAYER -> output.putEntityUniqueId(playerEid)
            TYPE_SHOW -> {
                output.putString(title)
                output.putLFloat(healthPercent)
                output.putLShort(unknownShort)
                output.putUnsignedVarInt(color)
                output.putUnsignedVarInt(overlay)
            }
            TYPE_UNKNOWN_6 -> {
                output.putLShort(unknownShort)
                output.putUnsignedVarInt(color)
                output.putUnsignedVarInt(overlay)
            }
            TYPE_TEXTURE -> {
                output.putUnsignedVarInt(color)
                output.putUnsignedVarInt(overlay)
            }
            TYPE_HEALTH_PERCENT -> output.putLFloat(healthPercent)
            TYPE_TITLE -> output.putString(title)
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleBossEvent(this)
    }

    companion object {
        const val TYPE_SHOW = 0
        const val TYPE_REGISTER_PLAYER = 1
        const val TYPE_HIDE = 2
        const val TYPE_UNREGISTER_PLAYER = 3
        const val TYPE_HEALTH_PERCENT = 4
        const val TYPE_TITLE = 5
        const val TYPE_UNKNOWN_6 = 6
        const val TYPE_TEXTURE = 7

        fun base(bossEntityUniqueId: Long, eventId: Int): BossEventPacket {
            val packet = BossEventPacket().apply {
                this.bossEid = bossEntityUniqueId
                this.eventType = eventId
            }

            return packet
        }

        fun show(
            bossEntityUniqueId: Long,
            title: String,
            healthPercent: Float,
            unknownShort: Int = 0
        ): BossEventPacket {
            val packet = base(bossEntityUniqueId, TYPE_SHOW)
            packet.title = title
            packet.healthPercent = healthPercent
            packet.unknownShort = unknownShort
            packet.color = 0
            packet.overlay = 0

            return packet
        }

        fun hide(bossEntityUniqueId: Long): BossEventPacket {
            return base(bossEntityUniqueId, TYPE_HIDE)
        }

        fun registerPlayer(bossEntityUniqueId: Long, playerEntityUniqueId: Long): BossEventPacket {
            val packet = base(bossEntityUniqueId, TYPE_REGISTER_PLAYER)
            packet.playerEid = playerEntityUniqueId
            return packet
        }

        fun unregisterPlayer(bossEntityUniqueId: Long, playerEntityUniqueId: Long): BossEventPacket {
            val packet = base(bossEntityUniqueId, TYPE_UNREGISTER_PLAYER)
            packet.playerEid = playerEntityUniqueId

            return packet
        }

        fun healthPercent(bossEntityUniqueId: Long, healthPercent: Float): BossEventPacket {
            val packet = base(bossEntityUniqueId, TYPE_HEALTH_PERCENT)
            packet.healthPercent = healthPercent

            return packet
        }

        fun title(bossEntityUniqueId: Long, title: String): BossEventPacket {
            val packet = base(bossEntityUniqueId, TYPE_TITLE)
            packet.title = title

            return packet
        }

        fun unknown6(bossEntityUniqueId: Long, unknownShort: Int): BossEventPacket {
            val packet = base(bossEntityUniqueId, TYPE_UNKNOWN_6)
            packet.unknownShort = unknownShort
            packet.color = 0
            packet.overlay = 0

            return packet
        }
    }
}
