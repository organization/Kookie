package be.zvz.kookie.network.mcpe.protocol

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

class SetDifficultyPacket : DataPacket(), ClientboundPacket, ServerboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.SET_DIFFICULTY_PACKET)

    var difficulty: Int

    static
    fun create(difficulty: Int): self {
        result = new self
                result.difficulty = difficulty
        return result
    }

    override fun decodePayload(input: PacketSerializer) {
        difficulty = input.getUnsignedVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putUnsignedVarInt(difficulty)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleSetDifficulty(this)
    }
}
