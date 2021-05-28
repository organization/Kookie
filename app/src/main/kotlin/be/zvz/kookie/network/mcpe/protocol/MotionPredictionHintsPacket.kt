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

class MotionPredictionHIntsPacket : DataPacket(), ClientboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.MOTION_PREDICTION_HINTS_PACKET)

	var entityRuntimeId: Int
	var motion: Vector3
	var onGround: Boolean

	 static fun create(entityRuntimeId: Int, motion: Vector3, onGround: Boolean) : self{
		result = new self
		result.entityRuntimeId = entityRuntimeId
		result.motion = motion
		result.onGround = onGround
		return result
	}

	 fun getEntityRuntimeId() : Int{ return entityRuntimeId }

	 fun getMotion() : Vector3{ return motion }

	 fun isOnGround() : Boolean{ return onGround }

	override fun decodePayload(input: PacketSerializer) {
		entityRuntimeId = input.getEntityRuntimeId()
		motion = input.getVector3()
		onGround = input.getBool()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putEntityRuntimeId(entityRuntimeId)
		output.putVector3(motion)
		output.putBool(onGround)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handleMotionPredictionHInts(this)
	}
}
