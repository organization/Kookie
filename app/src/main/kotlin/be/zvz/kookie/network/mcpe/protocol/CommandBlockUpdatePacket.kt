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

class CommandBlockUpdatePacket : DataPacket(), ServerboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.COMMAND_BLOCK_UPDATE_PACKET)

    var isBlock: Boolean

    var x: Int
    var y: Int
    var z: Int
    var commandBlockMode: Int
    var isRedstoneMode: Boolean
    var isConditional: Boolean

    var minecartEid: Int

    var command: string
    var lastOutput: string
    var name: string
    var shouldTrackOutput: Boolean
    var tickDelay: Int
    var executeOnFirstTick: Boolean

    override fun decodePayload(input: PacketSerializer) {
        isBlock = input.getBool()

        if (isBlock) {
            input.getBlockPosition(x, y, z)
            commandBlockMode = input.getUnsignedVarInt()
            isRedstoneMode = input.getBool()
            isConditional = input.getBool()
        } else {
            //Minecart with command block
            minecartEid = input.getEntityRuntimeId()
        }

        command = input.getString()
        lastOutput = input.getString()
        name = input.getString()

        shouldTrackOutput = input.getBool()
        tickDelay = input.getLInt()
        executeOnFirstTick = input.getBool()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putBool(isBlock)

        if (isBlock) {
            output.putBlockPosition(x, y, z)
            output.putUnsignedVarInt(commandBlockMode)
            output.putBool(isRedstoneMode)
            output.putBool(isConditional)
        } else {
            output.putEntityRuntimeId(minecartEid)
        }

        output.putString(command)
        output.putString(lastOutput)
        output.putString(name)

        output.putBool(shouldTrackOutput)
        output.putLInt(tickDelay)
        output.putBool(executeOnFirstTick)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleCommandBlockUpdate(this)
    }
}
