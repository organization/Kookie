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
package be.zvz.kookie.world.format.io.data

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.FloatTag
import be.zvz.kookie.nbt.tag.IntTag
import be.zvz.kookie.world.World
import be.zvz.kookie.world.format.io.WorldData
import be.zvz.kookie.world.format.io.exception.CorruptedWorldException
import be.zvz.kookie.world.format.io.exception.UnsupportedWorldFormatException
import java.nio.file.Path
import kotlin.io.path.exists

abstract class BaseNbtWorldData(protected val dataPath: Path) : WorldData {
    protected val compoundTag: CompoundTag

    final override val name: String
    final override val generatorName: String
    final override val generatorOptions: Map<String, String>
    final override val seed: Long

    final override var time: Long
        get() = compoundTag.getLong("Time", 0)
        set(value) {
            compoundTag.setLong("Time", value)
        }

    final override var spawn: Vector3
        get() = Vector3(
            compoundTag.getInt("SpawnX", 0),
            compoundTag.getInt("SpawnY", 0),
            compoundTag.getInt("SpawnZ", 0)
        )
        set(value) {
            compoundTag.setInt("SpawnX", value.floorX)
            compoundTag.setInt("SpawnY", value.floorY)
            compoundTag.setInt("SpawnZ", value.floorZ)
        }

    override var difficulty: Int
        get() = compoundTag.getByte("Difficulty", World.DIFFICULTY_NORMAL)
        set(value) {
            compoundTag.setByte("Difficulty", value)
        }

    override var rainTime: Int
        get() = compoundTag.getInt("rainTime", World.DIFFICULTY_NORMAL)
        set(value) {
            compoundTag.setInt("rainTime", value)
        }

    override var rainLevel: Float
        get() = (compoundTag.getTag("rainLevel") as? FloatTag)?.value // BE
            ?: compoundTag.getByte("raining", 0).toFloat() // JE
        set(value) {
            compoundTag.setFloat("rainLevel", value) // BE
            compoundTag.setByte("raining", value.toInt()) // JE
        }

    override var lightningTime: Int
        get() = (compoundTag.getTag("lightningLevel") as? IntTag)?.value // BE
            ?: compoundTag.getInt("thunderTime", 0) // JE
        set(value) {
            compoundTag.setInt("lightningTime", value) // BE
            compoundTag.setInt("thunderTime", value) // JE
        }

    override var lightningLevel: Float
        get() = (compoundTag.getTag("lightningLevel") as? FloatTag)?.value // BE
            ?: compoundTag.getByte("thundering", 0).toFloat() // JE
        set(value) {
            compoundTag.setFloat("lightningLevel", value) // BE
            compoundTag.setByte("thundering", value.toInt()) // JE
        }

    init {
        if (!dataPath.exists()) {
            throw CorruptedWorldException("World data not found at $dataPath")
        }

        compoundTag = try {
            this.load()
        } catch (e: CorruptedWorldException) {
            throw CorruptedWorldException("Corrupted world data: ${e.message}", e)
        }
        this.name = compoundTag.getString("LevelName")
        this.generatorName = compoundTag.getString("generatorName", "DEFAULT")
        this.generatorOptions = mapOf("preset" to compoundTag.getString("generatorOptions", ""))
        this.seed = compoundTag.getLong("RandomSeed")
        this.fix()
    }

    @Throws(CorruptedWorldException::class, UnsupportedWorldFormatException::class)
    protected abstract fun load(): CompoundTag

    @Throws(CorruptedWorldException::class, UnsupportedWorldFormatException::class)
    protected abstract fun fix()
}
