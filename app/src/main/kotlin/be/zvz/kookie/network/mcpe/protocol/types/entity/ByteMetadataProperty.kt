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
package be.zvz.kookie.network.mcpe.protocol.types.entity

import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

class ByteMetadataProperty(override var value: Int) : MetadataProperty(), IntegerishMetadataProperty {

    init {
        initialize(value)
    }

    override val id = EntityMetadataTypes.BYTE

    override fun write(output: PacketSerializer) {
        output.putByte(value)
    }

    override fun equals(other: Any?): Boolean = this === other ||
        javaClass == other?.javaClass &&
        value == (other as ByteMetadataProperty).value &&
        id == other.id

    override fun hashCode(): Int = value.let { 31 * it + id }

    override fun min(): Int = Byte.MIN_VALUE.toInt()

    override fun max(): Int = Byte.MAX_VALUE.toInt()

    override fun buildFromFlags(flags: Map<Int, Boolean>): IntegerishMetadataProperty {
        var value = 0
        flags.forEach { (flag, v) ->
            if (v) {
                value = value or (1 shl flag)
            }
        }
        return LongMetadataProperty(value)
    }

    companion object {
        @JvmStatic
        fun read(input: PacketSerializer) = ByteMetadataProperty(input.getByte())
    }
}
