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

class SettingsCommandPacket : DataPacket(), ServerboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.SETTINGS_COMMAND_PACKET)

    var command: string
    var suppressOutput: Boolean

    static
    fun create(command: string, suppressOutput: Boolean): self {
        result = new self
                result.command = command
        result.suppressOutput = suppressOutput
        return result
    }

    fun getCommand(): string {
        return command
    }

    fun getSuppressOutput(): Boolean {
        return suppressOutput
    }

    override fun decodePayload(input: PacketSerializer) {
        command = input.getString()
        suppressOutput = input.getBool()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putString(command)
        output.putBool(suppressOutput)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleSettingsCommand(this)
    }
}
