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

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer
import java.util.concurrent.atomic.AtomicInteger

class BlockPosMetadataProperty(val value: Vector3) : MetadataProperty() {
    override val id = EntityMetadataTypes.POS
    override fun write(output: PacketSerializer) {
        val floor = value.floor()
        output.putBlockPosition(floor.x.toInt(), floor.y.toInt(), floor.z.toInt())
    }

    override fun equals(other: Any?): Boolean {
        if (other != null) {
            other as BlockPosMetadataProperty
            return other.javaClass == this.javaClass && other.hashCode() == hashCode() && other.value == value
        }
        return false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + id
        return result
    }

    companion object {
        fun read(input: PacketSerializer): BlockPosMetadataProperty {
            val x = AtomicInteger()
            val y = AtomicInteger()
            val z = AtomicInteger()
            input.getBlockPosition(x, y, z)
            return BlockPosMetadataProperty(Vector3(x.get(), y.get(), z.get()))
        }
    }
}
