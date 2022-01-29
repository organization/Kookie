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

import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

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

    companion object {
        const val TYPE_SHOW = 0
        const val TYPE_REGISTER_PLAYER = 1
        const val TYPE_HIDE = 2
        const val TYPE_UNREGISTER_PLAYER = 3
        const val TYPE_HEALTH_PERCENT = 4
        const val TYPE_TITLE = 5
        const val TYPE_UNKNOWN_6 = 6
        const val TYPE_TEXTURE = 7

        @JvmStatic
        fun base(bossEntityUniqueId: Long, eventId: Int) = BossEventPacket().apply {
            bossEid = bossEntityUniqueId
            eventType = eventId
        }

        @JvmStatic
        fun show(
            bossEntityUniqueId: Long,
            title: String,
            healthPercent: Float,
            unknownShort: Int = 0
        ): BossEventPacket =
            base(bossEntityUniqueId, TYPE_SHOW).apply {
                this.title = title
                this.healthPercent = healthPercent
                this.unknownShort = unknownShort
                this.color = 0
                this.overlay = 0
            }

        @JvmStatic
        fun hide(bossEntityUniqueId: Long): BossEventPacket =
            base(bossEntityUniqueId, TYPE_HIDE)

        @JvmStatic
        fun registerPlayer(bossEntityUniqueId: Long, playerEntityUniqueId: Long) =
            base(bossEntityUniqueId, TYPE_REGISTER_PLAYER).apply {
                this.playerEid = playerEntityUniqueId
            }

        @JvmStatic
        fun unregisterPlayer(bossEntityUniqueId: Long, playerEntityUniqueId: Long) =
            base(bossEntityUniqueId, TYPE_UNREGISTER_PLAYER).apply {
                this.playerEid = playerEntityUniqueId
            }

        @JvmStatic
        fun healthPercent(bossEntityUniqueId: Long, healthPercent: Float) =
            base(bossEntityUniqueId, TYPE_HEALTH_PERCENT).apply {
                this.healthPercent = healthPercent
            }

        @JvmStatic
        fun title(bossEntityUniqueId: Long, title: String) =
            base(bossEntityUniqueId, TYPE_TITLE).apply {
                this.title = title
            }

        @JvmStatic
        fun unknown6(bossEntityUniqueId: Long, unknownShort: Int) =
            base(bossEntityUniqueId, TYPE_UNKNOWN_6).apply {
                this.unknownShort = unknownShort
                this.color = 0
                this.overlay = 0
            }
    }
}
