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

@ItemStackRequestIdentify(ItemStackRequestActionType.DESTROY)
class DestroyStackRequestAction(
    val count: Int,
    val source: ItemStackRequestSlotInfo
) : ItemStackRequestAction() {

    override fun write(out: PacketSerializer) {
        out.putByte(count)
        source.write(out)
    }

    companion object {
        @JvmStatic
        fun read(input: PacketSerializer) = CraftingConsumeInputStackRequestAction(
            count = input.getByte(),
            source = ItemStackRequestSlotInfo.read(input)
        )
    }
}
