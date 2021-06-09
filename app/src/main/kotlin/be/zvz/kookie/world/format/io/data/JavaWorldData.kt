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
package be.zvz.kookie.world.format.io.data

import be.zvz.kookie.nbt.BigEndianNbtSerializer
import be.zvz.kookie.nbt.NbtDataException
import be.zvz.kookie.nbt.TreeRoot
import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.FloatTag
import be.zvz.kookie.nbt.tag.StringTag
import be.zvz.kookie.world.World
import be.zvz.kookie.world.WorldCreationOptions
import be.zvz.kookie.world.format.io.exception.CorruptedWorldException
import be.zvz.kookie.world.generator.GeneratorManager
import java.nio.file.Path
import java.util.zip.DeflaterOutputStream
import java.util.zip.InflaterInputStream
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

class JavaWorldData(dataPath: Path) : BaseNbtWorldData(dataPath) {
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
        get() {
            val rainLevelTag = compoundTag.getTag("rainLevel") // BE
            return if (rainLevelTag is FloatTag) {
                rainLevelTag.value
            } else {
                compoundTag.getByte("raining", 0).toFloat() // JE
            }
        }
        set(value) {
            compoundTag.setFloat("rainLevel", value) // BE
            compoundTag.setByte("raining", value.toInt()) // JE
        }

    override var lightningTime: Int
        get() = compoundTag.getInt("lightningTime", World.DIFFICULTY_NORMAL)
        set(value) {
            compoundTag.setInt("lightningTime", value)
        }

    override var lightningLevel: Float
        get() {
            val rainLevelTag = compoundTag.getTag("lightningLevel") // BE
            return if (rainLevelTag is FloatTag) {
                rainLevelTag.value
            } else {
                compoundTag.getByte("thundering", 0).toFloat() // JE
            }
        }
        set(value) {
            compoundTag.setFloat("lightningLevel", value) // BE
            compoundTag.setByte("thundering", value.toInt()) // JE
        }

    override fun fix() {
        if (compoundTag.getTag("generatorName") !is StringTag) {
            compoundTag.setString("generatorName", "default")
        }

        if (compoundTag.getTag("generatorOptions") !is StringTag) {
            compoundTag.setString("generatorOptions", "")
        }
    }

    @Throws(CorruptedWorldException::class)
    override fun load(): CompoundTag {
        val rawLevelData: String = InflaterInputStream(dataPath.inputStream()).readBytes().toString()
        if (rawLevelData.isEmpty()) {
            throw CorruptedWorldException("Failed to read level.dat (permission denied or doesn't exist)")
        }

        val worldData: TreeRoot = try {
            BigEndianNbtSerializer().read(rawLevelData)
        } catch (e: NbtDataException) {
            throw CorruptedWorldException(e.message ?: "Failed to read level.dat (nbt read failed)", e)
        }

        return try {
            worldData.mustGetCompoundTag().getCompoundTag("Data")
                ?: throw CorruptedWorldException("Failed to read level.dat  (Missing 'Data' key or wrong type)")
        } catch (e: NbtDataException) {
            throw CorruptedWorldException("Failed to read level.dat (${e.message ?: "nbt read failed"})", e)
        }
    }

    override fun save() {
        val worldData = TreeRoot(CompoundTag().apply { setTag("Data", compoundTag) })
        val rawLevelData = BigEndianNbtSerializer().write(worldData)
        DeflaterOutputStream(dataPath.outputStream()).write(rawLevelData.toByteArray())
    }

    companion object {
        fun generate(dataPath: Path, name: String, options: WorldCreationOptions, version: Int = 19133) {
            val worldData = TreeRoot(
                CompoundTag().apply {
                    setByte("hardcore", 0)
                    setByte("Difficulty", options.difficulty)
                    setByte("initialized", 1)
                    setInt("GameType", 0)
                    setInt("generatorVersion", 1)
                    setInt("SpawnX", options.spawnPosition.x.toInt())
                    setInt("SpawnY", options.spawnPosition.y.toInt())
                    setInt("SpawnZ", options.spawnPosition.z.toInt())
                    setInt("version", version)
                    setInt("DayTime", 0)
                    setLong("LastPlayed", System.currentTimeMillis())
                    setLong("RandomSeed", options.seed)
                    setLong("SizeOnDisk", 0)
                    setLong("Time", 0)
                    setString("generatorName", GeneratorManager.getGeneratorName(options.generatorClass))
                    setString("generatorOptions", options.generatorOptions)
                    setString("LevelName", name)
                    setTag("GameRules", CompoundTag())
                }
            )
            val rawLevelData = BigEndianNbtSerializer().write(worldData)
            DeflaterOutputStream(dataPath.resolve("level.dat").outputStream()).write(rawLevelData.toByteArray())
        }
    }
}
