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

class ReleaseItemTransactionData : TransactionData() {

    override val typeId = InventoryTransactionPacket.TYPE_RELEASE_ITEM

    private var actionType: Int = -1

    private var hotbarSlot: Int = -1

    private lateinit var itemInHand: ItemStackWrapper

    private lateinit var headPos: Vector3

    fun getActionType(): Int = actionType

    fun getHotbarSlot(): Int = hotbarSlot

    fun getItemInHand(): ItemStackWrapper = itemInHand

    fun getHeadPos(): Vector3 = headPos

    override fun decodeData(input: PacketSerializer) {
        actionType = input.getUnsignedVarInt()
        hotbarSlot = input.getVarInt()
        itemInHand = ItemStackWrapper.read(input)
        headPos = input.getVector3()
    }

    override fun encodeData(output: PacketSerializer) {
        output.putUnsignedVarInt(actionType)
        output.putVarInt(hotbarSlot)
        itemInHand.write(output)
        output.putVector3(headPos)
    }

    companion object {
        fun new(
            actions: MutableList<NetworkInventoryAction>,
            actionType: Int,
            hotbarSlot: Int,
            itemInHand: ItemStackWrapper,
            headPos: Vector3
        ): ReleaseItemTransactionData = ReleaseItemTransactionData().apply {
            this.setActions(actions)
            this.actionType = actionType
            this.hotbarSlot = hotbarSlot
            this.itemInHand = itemInHand
            this.headPos = headPos
        }
    }
}
