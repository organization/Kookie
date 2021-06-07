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
package be.zvz.kookie.network.mcpe.protocol.types.inventory

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.protocol.InventoryTransactionPacket
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

class UseItemOnEntityTransactionData : TransactionData() {

    override val typeId = InventoryTransactionPacket.TYPE_USE_ITEM_ON_ENTITY

    private var entityRuntimeId: Long = -1L

    private var actionType: Int = -1

    private var hotbarSlot: Int = -1

    private lateinit var itemInHand: ItemStackWrapper

    private lateinit var playerPos: Vector3

    private lateinit var clickPos: Vector3

    fun getActionType(): Int = actionType

    fun getHotbarSlot(): Int = hotbarSlot

    fun getItemInHand(): ItemStackWrapper = itemInHand

    fun getPlayerPos(): Vector3 = playerPos

    fun getClickPos(): Vector3 = clickPos

    override fun decodeData(input: PacketSerializer) {
        entityRuntimeId = input.getEntityRuntimeId()
        actionType = input.getUnsignedVarInt()
        hotbarSlot = input.getVarInt()
        itemInHand = ItemStackWrapper.read(input)
        playerPos = input.getVector3()
        clickPos = input.getVector3()
    }

    override fun encodeData(output: PacketSerializer) {
        output.putEntityRuntimeId(entityRuntimeId)
        output.putUnsignedVarInt(actionType)
        output.putVarInt(hotbarSlot)
        itemInHand.write(output)
        output.putVector3(playerPos)
        output.putVector3(clickPos)
    }

    companion object {
        const val ACTION_INTERACT = 0
        const val ACTION_ATTACK = 1

        fun new(
            actions: MutableList<NetworkInventoryAction>,
            entityRuntimeId: Long,
            actionType: Int,
            hotbarSlot: Int,
            itemInHand: ItemStackWrapper,
            playerPos: Vector3,
            clickPos: Vector3
        ): UseItemOnEntityTransactionData = UseItemOnEntityTransactionData().apply {
            this.setActions(actions)
            this.entityRuntimeId = entityRuntimeId
            this.actionType = actionType
            this.hotbarSlot = hotbarSlot
            this.itemInHand = itemInHand
            this.playerPos = playerPos
            this.clickPos = clickPos
        }
    }
}
