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
use fun count

class UpdateSoftEnumPacket : DataPacket(), ClientboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.UPDATE_SOFT_ENUM_PACKET)

    const val TYPE_ADD = 0
    const val TYPE_REMOVE = 1
    const val TYPE_SET = 2

    var enumName: string
    /** @var string[] */
    values = []
    var type: Int

    override fun decodePayload(input: PacketSerializer) {
        enumName = input.getString()
        for (i = 0, count = input.getUnsignedVarInt() i < count++i){
            values[] = input.getString()
        }
        type = input.getByte()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putString(enumName)
        output.putUnsignedVarInt(count(values))
        foreach(values v : as) {
            output.putString(v)
        }
        output.putByte(type)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleUpdateSoftEnum(this)
    }
}
