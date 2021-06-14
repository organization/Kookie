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
import be.zvz.kookie.network.mcpe.protocol.types.EnchantOption

@ProtocolIdentify(ProtocolInfo.IDS.PLAYER_ENCHANT_OPTIONS_PACKET)
class PlayerEnchantOptionsPacket : DataPacket(), ClientboundPacket {

    lateinit var options: List<EnchantOption>

    override fun decodePayload(input: PacketSerializer) {
        options = mutableListOf<EnchantOption>().apply {
            for (i in 0 until input.getUnsignedVarInt()) {
                add(EnchantOption.read(input))
            }
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putUnsignedVarInt(options.size)
        options.forEach {
            it.write(output)
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handlePlayerEnchantOptions(this)
    }

    companion object {
        @JvmStatic
        fun create(options: List<EnchantOption>) = PlayerEnchantOptionsPacket().apply {
            this.options = options
        }
    }
}
