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
 * Copyright (C) 2021 - 2022 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.network.mcpe.protocol.types.entity

import be.zvz.kookie.nbt.LittleEndianNbtSerializer
import be.zvz.kookie.nbt.TreeRoot
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

class CompoundMetadataProperty(var value: CompoundTag) : MetadataProperty() {

    override val id = EntityMetadataTypes.COMPOUND_TAG
    override fun write(output: PacketSerializer) {
        output.put((LittleEndianNbtSerializer()).write(TreeRoot(value)))
    }

    override fun equals(other: Any?): Boolean = this === other ||
        javaClass == other?.javaClass &&
        value == (other as CompoundMetadataProperty).value &&
        id == other.id

    override fun hashCode(): Int = super.hashCode().let { 31 * it + value.hashCode() }.let { 31 * it + id }

    companion object {
        @JvmStatic
        fun read(input: PacketSerializer) = CompoundMetadataProperty(input.getNbtCompoundRoot())
    }
}
