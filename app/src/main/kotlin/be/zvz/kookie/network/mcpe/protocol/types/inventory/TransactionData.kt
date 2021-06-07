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

import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

abstract class TransactionData {

    private var actions: MutableList<NetworkInventoryAction> = mutableListOf()

    abstract val typeId: Int

    fun getActions(): MutableList<NetworkInventoryAction> {
        return actions
    }

    protected fun setActions(actions: MutableList<NetworkInventoryAction>) {
        this.actions = actions
    }

    fun decode(input: PacketSerializer) {
        for (i in 0 until input.getUnsignedVarInt()) {
            actions.add(NetworkInventoryAction().read(input))
        }
        decodeData(input)
    }

    fun encode(output: PacketSerializer) {
        output.putUnsignedVarInt(actions.size)
        actions.forEach {
            it.write(output)
        }
        encodeData(output)
    }

    abstract fun decodeData(input: PacketSerializer)

    abstract fun encodeData(output: PacketSerializer)
}
