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

class PositionTrackingDBServerBroadcastPacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.POSITION_TRACKING_D_B_SERVER_BROADCAST_PACKET)

	 const val ACTION_UPDATE = 0
	 const val ACTION_DESTROY = 1
	 const val ACTION_NOT_FOUND = 2

	var action: Int
	var trackingId: Int
	/**
	 * @var CacheableNbt
	 * @phpstan-var CacheableNbt<\pocketmine\nbt\tag\CompoundTag>
	 */
	 nbt

	/**
	 * @phpstan-param CacheableNbt<\pocketmine\nbt\tag\CompoundTag> nbt
	 */
	 static fun create(action: Int, trackingId: Int, nbt: CacheableNbt) : self{
		result = new self
		result.action = action
		result.trackingId = trackingId
		result.nbt = nbt
		return result
	}

	 fun getAction() : Int{ return action }

	 fun getTrackingId() : Int{ return trackingId }

	/** @phpstan-return CacheableNbt<\pocketmine\nbt\tag\CompoundTag> */
	 fun getNbt() : CacheableNbt{ return nbt }

	override fun decodePayload(input: PacketSerializer) {
		action = input.getByte()
		trackingId = input.getVarInt()
		nbt = new CacheableNbt(input.getNbtCompoundRoot())
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putByte(action)
		output.putVarInt(trackingId)
		output.put(nbt->getEncodedNbt())
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handlePositionTrackingDBServerBroadcast(this)
	}
}
