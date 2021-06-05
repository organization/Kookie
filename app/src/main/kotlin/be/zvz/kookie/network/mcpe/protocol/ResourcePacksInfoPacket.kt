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
package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.types.resourcepack.BehaviorPackEntry
import be.zvz.kookie.network.mcpe.protocol.types.resourcepack.ResourcePackEntry
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.RESOURCE_PACKS_INFO_PACKET)
class ResourcePacksInfoPacket : DataPacket(), ClientboundPacket {

    var mustAccept: Boolean = false

    /**
     * If true, any client which doesn't have scripting system will cause disconnect
     * Currently only Windows10 edition is supported
     */
    var hasScript: Boolean = false
    val behaviorPackEntries = mutableListOf<BehaviorPackEntry>()
    val resourcePackEntries = mutableListOf<ResourcePackEntry>()

    override fun decodePayload(input: PacketSerializer) {
        mustAccept = input.getBoolean()
        hasScript = input.getBoolean()
        val behaviorPackCount = input.getLShort()
        for (i in 0 until behaviorPackCount) {
            behaviorPackEntries.add(BehaviorPackEntry.read(input))
        }
        val resourcePackCount = input.getLShort()
        for (i in 0 until resourcePackCount) {
            resourcePackEntries.add(ResourcePackEntry.read(input))
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putBoolean(mustAccept)
        output.putBoolean(hasScript)
        output.putLShort(behaviorPackEntries.size)
        behaviorPackEntries.forEach {
            it.write(output)
        }
        output.putLShort(resourcePackEntries.size)
        resourcePackEntries.forEach {
            it.write(output)
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleResourcePacksInfo(this)
    }
}
