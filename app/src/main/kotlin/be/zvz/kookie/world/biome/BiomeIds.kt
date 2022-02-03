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
package be.zvz.kookie.world.biome

enum class BiomeIds(val id: Int, val biomeName: String) {
    UNKNOWN(-1, "Unknown"),
    OCEAN(0, "Ocean"),
    PLAINS(1, "Plains"),
    DESERT(2, "Desert"),
    MOUNTAINS(3, "Mountains"),
    FOREST(4, "Forest"),
    TAIGA(5, "Taiga"),
    SWAMP(6, "Swamp"),
    RIVER(7, "River"),

    HELL(8, "Hell"),

    ICE_PLAINS(12, "Ice Plains"),

    SMALL_MOUNTAINS(20, "Small Mountains"),

    BIRCH_FOREST(27, "Bitch Forest"),
}
