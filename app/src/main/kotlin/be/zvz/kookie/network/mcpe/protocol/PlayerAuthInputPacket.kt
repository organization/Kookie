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
use fun assert

class PlayerAuthInputPacket : DataPacket(), ServerboundPacket{
@ProtocolIdentify(ProtocolInfo.IDS.PLAYER_AUTH_INPUT_PACKET)

	var position: Vector3
	var pitch: Float
	var yaw: Float
	var headYaw: Float
	var moveVecX: Float
	var moveVecZ: Float
	var inputputFlags: Int
	var inputputMode: Int
	var playMode: Int
	var vrGazeDirection: Vector3|null = null
	var tick: Int
	var delta: Vector3

	/**
	 * @param Int          inputputFlags @see InputFlags
	 * @param Int          inputputMode @see InputMode
	 * @param Int          playMode @see PlayMode
	 * @param Vector3|null vrGazeDirection only used when PlayMode::VR
	 */
	 static fun create(position: Vector3, pitch: Float, yaw: Float, headYaw: Float, moveVecX: Float, moveVecZ: Float, inputputFlags: Int, inputputMode: Int, playMode: Int, ?vrGazeDirection: Vector3, tick: Int, delta: Vector3) : self{
		if(playMode === PlayMode::VR and vrGazeDirection === null){
			//yuck, can we get a properly written packet just once? ...
			throw new \InvalidArgumentException("Gaze direction must be provided for VR play mode")
		}
		result = new self
		result.position = position.asVector3()
		result.pitch = pitch
		result.yaw = yaw
		result.headYaw = headYaw
		result.moveVecX = moveVecX
		result.moveVecZ = moveVecZ
		result.inputFlags = inputputFlags
		result.inputMode = inputputMode
		result.playMode = playMode
		if(vrGazeDirection !== null){
			result.vrGazeDirection = vrGazeDirection.asVector3()
		}
		result.tick = tick
		result.delta = delta
		return result
	}

	 fun getPosition() : Vector3{
		return position
	}

	 fun getPitch() : Float{
		return pitch
	}

	 fun getYaw() : Float{
		return yaw
	}

	 fun getHeadYaw() : Float{
		return headYaw
	}

	 fun getMoveVecX() : Float{
		return moveVecX
	}

	 fun getMoveVecZ() : Float{
		return moveVecZ
	}

	/**
	 * @see PlayerAuthInputFlags
	 */
	 fun getInputFlags() : Int{
		return inputFlags
	}

	/**
	 * @see InputMode
	 */
	 fun getInputMode() : Int{
		return inputMode
	}

	/**
	 * @see PlayMode
	 */
	 fun getPlayMode() : Int{
		return playMode
	}

	 fun getVrGazeDirection() : ?Vector3{
		return vrGazeDirection
	}

	override fun decodePayload(input: PacketSerializer) {
		pitch = input.getLFloat()
		yaw = input.getLFloat()
		position = input.getVector3()
		moveVecX = input.getLFloat()
		moveVecZ = input.getLFloat()
		headYaw = input.getLFloat()
		inputFlags = input.getUnsignedVarLong()
		inputMode = input.getUnsignedVarInt()
		playMode = input.getUnsignedVarInt()
		if(playMode === PlayMode::VR){
			vrGazeDirection = input.getVector3()
		}
		tick = input.getUnsignedVarLong()
		delta = input.getVector3()
	}

	override fun encodePayload(output: PacketSerializer) {
		output.putLFloat(pitch)
		output.putLFloat(yaw)
		output.putVector3(position)
		output.putLFloat(moveVecX)
		output.putLFloat(moveVecZ)
		output.putLFloat(headYaw)
		output.putUnsignedVarLong(inputFlags)
		output.putUnsignedVarInt(inputMode)
		output.putUnsignedVarInt(playMode)
		if(playMode === PlayMode::VR){
			assert(vrGazeDirection !== null)
			output.putVector3(vrGazeDirection)
		}
		output.putUnsignedVarLong(tick)
		output.putVector3(delta)
	}

	 override fun handle(handler: PacketHandlerInterface) : Boolean{
		return handler.handlePlayerAuthInput(this)
	}
}
