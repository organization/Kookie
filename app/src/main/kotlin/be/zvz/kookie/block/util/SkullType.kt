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
package be.zvz.kookie.block.util

enum class SkullType(val displayName: String, val magicNumber: Int) {
    SKELETON("Skeleton Skull", 0),
    WITHER_SKELETON("Wither Skeleton Skull", 1),
    ZOMBIE("Zombie Head", 2),
    PLAYER("Player Head", 3),
    CREEPER("Creeper Head", 4),
    DRAGON("Dragon Head", 5),
}
