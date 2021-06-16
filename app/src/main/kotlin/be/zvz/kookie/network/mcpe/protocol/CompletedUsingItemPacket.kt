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

@ProtocolIdentify(ProtocolInfo.IDS.COMPLETED_USING_ITEM_PACKET)
class CompletedUsingItemPacket : DataPacket(), ClientboundPacket {

    var itemId: Int = 0
    var action: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        itemId = input.getShort()
        action = input.getLInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putShort(itemId)
        output.putLInt(action)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handleCompletedUsingItem(this)

    enum class Action(val action: Int) {
        UNKNOWN(-1),
        EQUIP_ARMOR(0),
        EAT(1),
        ATTACK(2),
        CONSUME(3),
        THROW(4),
        SHOOT(5),
        PLACE(6),
        FILL_BOTTLE(7),
        FILL_BUCKET(8),
        POUR_BUCKET(9),
        USE_TOOL(10),
        INTERACT(11),
        RETRIEVED(12),
        DYED(13),
        TRADED(14),
    }
}
