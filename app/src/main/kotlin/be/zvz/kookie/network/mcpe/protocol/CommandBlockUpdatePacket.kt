package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

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

@ProtocolIdentify(ProtocolInfo.IDS.COMMAND_BLOCK_UPDATE_PACKET)
class CommandBlockUpdatePacket : DataPacket(), ServerboundPacket {
    var isBlock: Boolean = false

    var x: Int = 0
    var y: Int = 0
    var z: Int = 0
    var commandBlockMode: Int = 0
    var isRedstoneMode: Boolean = false
    var isConditional: Boolean = false

    var minecartEid: Long = 0

    lateinit var command: String
    lateinit var lastOutput: String
    lateinit var identifier: String
    var shouldTrackOutput: Boolean = false
    var tickDelay: Int = 0
    var executeOnFirstTick: Boolean = false

    override fun decodePayload(input: PacketSerializer) {
        isBlock = input.getBoolean()

        if (isBlock) {
            input.getBlockPosition(PacketSerializer.BlockPosition(x, y, z))
            commandBlockMode = input.getUnsignedVarInt()
            isRedstoneMode = input.getBoolean()
            isConditional = input.getBoolean()
        } else {
            // Minecart with command block
            minecartEid = input.getEntityRuntimeId()
        }

        command = input.getString()
        lastOutput = input.getString()
        identifier = input.getString()

        shouldTrackOutput = input.getBoolean()
        tickDelay = input.getLInt()
        executeOnFirstTick = input.getBoolean()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putBoolean(isBlock)

        if (isBlock) {
            output.putBlockPosition(x, y, z)
            output.putUnsignedVarInt(commandBlockMode)
            output.putBoolean(isRedstoneMode)
            output.putBoolean(isConditional)
        } else {
            output.putEntityRuntimeId(minecartEid)
        }

        output.putString(command)
        output.putString(lastOutput)
        output.putString(identifier)

        output.putBoolean(shouldTrackOutput)
        output.putLInt(tickDelay)
        output.putBoolean(executeOnFirstTick)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleCommandBlockUpdate(this)
    }
}
