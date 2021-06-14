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

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.PlayMode

@ProtocolIdentify(ProtocolInfo.IDS.PLAYER_AUTH_INPUT_PACKET)
class PlayerAuthInputPacket : DataPacket(), ServerboundPacket {

    lateinit var position: Vector3
    var pitch: Float = 0.0f
    var yaw: Float = 0.0f
    var headYaw: Float = 0.0f
    var moveVecX: Float = 0.0f
    var moveVecZ: Float = 0.0f
    var inputFlags: Long = 0
    var inputMode: Int = 0
    var playMode: Int = 0
    lateinit var vrGazeDirection: Vector3
    var tick: Long = 0
    lateinit var delta: Vector3

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
        if (playMode == PlayMode.VR.mode) {
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
        if (playMode == PlayMode.VR.mode && this::vrGazeDirection.isInitialized) {
            output.putVector3(vrGazeDirection)
        }
        output.putUnsignedVarLong(tick)
        output.putVector3(delta)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = handler.handlePlayerAuthInput(this)

    companion object {
        @JvmStatic
        fun create(
            position: Vector3,
            pitch: Float,
            yaw: Float,
            headYaw: Float,
            moveVecX: Float,
            moveVecZ: Float,
            inputFlags: Long,
            inputMode: Int,
            playMode: Int,
            vrGazeDirection: Vector3?,
            tick: Long,
            delta: Vector3
        ) = PlayerAuthInputPacket().apply {
            if (playMode == PlayMode.VR.mode && vrGazeDirection === null) {
                // yuck, can we get a properly written packet just once? ...
                throw IllegalArgumentException("Gaze direction must be provided for VR play mode")
            }
            this.position = position.asVector3()
            this.pitch = pitch
            this.yaw = yaw
            this.headYaw = headYaw
            this.moveVecX = moveVecX
            this.moveVecZ = moveVecZ
            this.inputFlags = inputFlags
            this.inputMode = inputMode
            this.playMode = playMode
            vrGazeDirection?.let { this.vrGazeDirection = vrGazeDirection.asVector3() }
            this.tick = tick
            this.delta = delta
        }
    }
}
