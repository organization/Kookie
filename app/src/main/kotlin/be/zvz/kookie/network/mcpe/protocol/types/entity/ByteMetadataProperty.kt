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

    override fun min(): Int {
        return Byte.MIN_VALUE.toInt()
    }

    override fun max(): Int {
        return Byte.MAX_VALUE.toInt()
    }

    override fun buildFromFlags(flags: Map<Int, Boolean>): IntegerishMetadataProperty {
        var value = 0
        flags.forEach { (flag, v) ->
            if (v) {
                value = value or (1 shl flag)
            }
        }
        return LongMetadataProperty(value)
    }

    override fun hashCode(): Int {
        var result = value
        result = 31 * result + id
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LongMetadataProperty

        if (value != other.value) return false
        if (id != other.id) return false

        return true
    }

    companion object {
        fun read(input: PacketSerializer): ByteMetadataProperty {
            return ByteMetadataProperty(input.getByte())
        }
    }
}
