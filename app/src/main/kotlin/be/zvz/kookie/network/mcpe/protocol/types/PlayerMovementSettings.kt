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
package be.zvz.kookie.network.mcpe.protocol.types

import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

class PlayerMovementSettings(
    private val movementType: Int,
    private val rewindSize: Int,
    private val serverAuthoritativeBlockBreaking: Boolean
) {

    fun getMovementType(): Int = movementType

    fun getRewindSize(): Int = rewindSize

    fun isServerAuthoritativeBlockBreaking(): Boolean = serverAuthoritativeBlockBreaking

    fun write(output: PacketSerializer) {
        /*
        $out->putVarInt($this->movementType);
		$out->putVarInt($this->rewindHistorySize);
		$out->putBool($this->serverAuthoritativeBlockBreaking);
         */
        output.putVarInt(movementType)
        output.putVarInt(rewindSize)
        output.putBoolean(serverAuthoritativeBlockBreaking)
    }

    companion object {
        @JvmStatic
        fun read(input: PacketSerializer): PlayerMovementSettings {
            return PlayerMovementSettings(
                input.getVarInt(),
                input.getVarInt(),
                input.getBoolean()
            )
        }
    }
}
