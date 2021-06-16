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
package be.zvz.kookie.network.mcpe.protocol.types.inventory.stackrequest

import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

@ItemStackRequestIdentify(ItemStackRequestActionType.MINE_BLOCK)
class MineBlockStackRequestAction(
    val unknown1: Int,
    val predictedDurability: Int,
    val stackId: Int
) : ItemStackRequestAction() {
    override fun write(out: PacketSerializer) {
        out.putVarInt(unknown1)
        out.putVarInt(predictedDurability)
        out.writeGenericTypeNetworkId(stackId)
    }

    companion object {
        @JvmStatic
        fun read(input: PacketSerializer) = MineBlockStackRequestAction(
            unknown1 = input.getVarInt(),
            predictedDurability = input.getVarInt(),
            stackId = input.readGenericTypeNetworkId()
        )
    }
}
