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
 * Copyright (C) 2021 - 2022 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.network.mcpe.protocol.types.inventory

import be.zvz.kookie.network.mcpe.protocol.PacketHandlingException
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import com.nukkitx.protocol.bedrock.data.inventory.TransactionType

class MismatchTransactionData : TransactionData() {

    override val typeId = TransactionType.INVENTORY_MISMATCH

    override fun decodeData(input: PacketSerializer) {
        if (getActions().size > 0) {
            throw PacketHandlingException(
                "Mismatch transaction type should not have any actions associated with it, but got ${getActions().size}"
            )
        }
    }

    override fun encodeData(output: PacketSerializer) {
    }
}
