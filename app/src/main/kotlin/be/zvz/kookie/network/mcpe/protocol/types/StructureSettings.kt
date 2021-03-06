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
package be.zvz.kookie.network.mcpe.protocol.types

import be.zvz.kookie.math.Vector3

class StructureSettings {
    var paletteName: String = ""
    var ignoreEntities: Boolean = false
    var ignoreBlocks: Boolean = false
    var structureSizeX: Int = 0
    var structureSizeY: Int = 0
    var structureSizeZ: Int = 0
    var structureOffsetX: Int = 0
    var structureOffsetY: Int = 0
    var structureOffsetZ: Int = 0
    var lastTouchedByPlayerID: Long = 0
    var rotation: Int = 0
    var mirror: Int = 0
    var integrityValue: Float = 0F
    var integritySeed: Int = 0
    lateinit var pivot: Vector3
}
