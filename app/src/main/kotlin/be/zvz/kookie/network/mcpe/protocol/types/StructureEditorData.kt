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

class StructureEditorData {
    enum class Type(val id: Int) {
        DATA(0),
        SAVE(1),
        LOAD(2),
        CORNER(3),
        INVALID(4),
        EXPORT(5)
    }

    lateinit var structureName: String
    lateinit var structureDataField: String
    var includePlayers: Boolean = false
    var showBoundingBox: Boolean = false
    var structureBlockType: Int = 0
    lateinit var structureSettings: StructureSettings
    var structureRedstoneSaveMove: Int = 0
}
