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

class PlayerFogPacket : DataPacket(), ClientboundPacket {
    @ProtocolIdentify(ProtocolInfo.IDS.PLAYER_FOG_PACKET)

    /**
     * @var string[]
     * @phpstan-var list<string>
     */
    fogLayers

    /**
     * @param string[] fogLayers
     * @phpstan-param list<string> fogLayers
     */
    static
    fun create(fogLayers: array): self {
        result = new self
                result.fogLayers = fogLayers
        return result
    }

    /**
     * @return string[]
     * @phpstan-return list<string>
     */
    fun getFogLayers(): array {
        return fogLayers
    }

    override fun decodePayload(input: PacketSerializer) {
        fogLayers = []
        for (i = 0, len = input.getUnsignedVarInt() i < len++i){
            fogLayers[] = input.getString()
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putUnsignedVarInt(count(fogLayers))
        foreach(fogLayers fogLayer : as) {
            output.putString(fogLayer)
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handlePlayerFog(this)
    }
}
