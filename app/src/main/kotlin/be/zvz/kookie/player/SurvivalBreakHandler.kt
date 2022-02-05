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
package be.zvz.kookie.player

import be.zvz.kookie.block.Block
import be.zvz.kookie.entity.animation.ArmSwingAnimation
import be.zvz.kookie.math.Facing
import be.zvz.kookie.math.Vector3
import be.zvz.kookie.world.particle.BlockPunchParticle
import be.zvz.kookie.world.sound.BlockPunchSound
import com.nukkitx.math.vector.Vector3f
import com.nukkitx.protocol.bedrock.data.LevelEventType
import com.nukkitx.protocol.bedrock.packet.LevelEventPacket
import kotlin.math.abs
import kotlin.math.pow

class SurvivalBreakHandler @JvmOverloads constructor(
    val player: Player,
    val blockPos: Vector3,
    val block: Block,
    val targetedFace: Facing,
    val maxPlayerDistance: Int,
    val fxTicKInterval: Int = DEFAULT_FX_INTERVAL_TICKS
) {

    var fxTicker: Int = 0

    var breakSpeed: Float = calculateBreakProgressPerTick()

    var breakProgress: Float = 0f

    init {
        if (breakSpeed > 0) {
            player.world.broadcastPacketToViewers(
                blockPos,
                LevelEventPacket().apply {
                    type = LevelEventType.BLOCK_START_BREAK
                    data = (65535 * breakSpeed).toInt()
                    position = Vector3f.ZERO.add(blockPos.x, blockPos.y, blockPos.z)
                }
            )
        }
    }

    /**
     * Returns the calculated break speed as percentage progress per game tick.
     */
    private fun calculateBreakProgressPerTick(): Float {
        if (!block.breakInfo.isBreakable()) {
            return 0f
        }
        // TODO: improve this to take stuff like swimming, ladders, enchanted tools into account, fix wrong tool break time calculations for bad tools (pmmp/PocketMine-MP#211)
        val breakTimePerTick = block.breakInfo.getBreakTime(player.inventory.getItemInHand()) * 20
        if (breakTimePerTick > 0) {
            return 1 / breakTimePerTick
        }
        return 1f
    }

    fun update(): Boolean {
        if (player.getPosition().distanceSquared(blockPos.add(0.5, 0.5, 0.5)) > maxPlayerDistance.toDouble().pow(2)) {
            return false
        }
        val newBreakSpeed = calculateBreakProgressPerTick()
        if (abs(newBreakSpeed - breakSpeed) > 0.0001) {
            breakSpeed = newBreakSpeed
            // TODO: sync with client
        }
        breakProgress += breakSpeed

        if ((fxTicker++ % fxTicKInterval) == 0 && breakProgress < 1) {
            player.world.addParticle(blockPos, BlockPunchParticle(block, targetedFace))
            player.world.addSound(blockPos, BlockPunchSound(block))
            player.broadcastAnimation(
                ArmSwingAnimation(player),
                mutableListOf<Player>().apply {
                    player.getViewers().forEach { (_, p) ->
                        add(p)
                    }
                }
            )
        }
        return breakProgress < 1
    }

    fun close() {
        if (player.world.isInLoadedTerrain(blockPos)) {
            player.world.broadcastPacketToViewers(
                blockPos,
                LevelEventPacket().apply {
                    type = LevelEventType.BLOCK_STOP_BREAK
                    data = 0
                    position = Vector3f.ZERO.add(blockPos.x, blockPos.y, blockPos.z)
                }
            )
        }
    }

    companion object {
        const val DEFAULT_FX_INTERVAL_TICKS = 5
    }
}
