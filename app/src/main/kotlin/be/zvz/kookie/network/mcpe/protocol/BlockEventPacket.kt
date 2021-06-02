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
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer
import java.util.concurrent.atomic.AtomicInteger

@ProtocolIdentify(ProtocolInfo.IDS.BLOCK_EVENT_PACKET)
class BlockEventPacket : DataPacket(), ClientboundPacket {

    /*
    /** @var int */
	public $x;
	/** @var int */
	public $y;
	/** @var int */
	public $z;
	/** @var int */
	public $eventType;
	/** @var int */
	public $eventData;
     */

    var x: AtomicInteger = AtomicInteger()
    var y: AtomicInteger = AtomicInteger()
    var z: AtomicInteger = AtomicInteger()

    var eventType: Int = 0
    var eventData: Int = 0

    override fun decodePayload(input: PacketSerializer) {
        input.getBlockPosition(x, y, z)
        eventType = input.getVarInt()
        eventData = input.getVarInt()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putBlockPosition(x.get(), y.get(), z.get())
        output.putVarInt(eventType)
        output.putVarInt(eventData)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        TODO("Not yet implemented")
    }

    companion object {
        const val EVENT_CHEST = 1

        const val CHEST_OPEN = 0
        const val CHEST_CLOSE = 1
    }
}
