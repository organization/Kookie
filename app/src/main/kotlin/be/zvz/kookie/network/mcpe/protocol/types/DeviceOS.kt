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
package be.zvz.kookie.network.mcpe.protocol.types

enum class DeviceOS(val id: Int) {
    UNKNOWN(-1),
    ANDROID(1),
    IOS(2),
    OSX(3),
    AMAZON(4),
    GEAR_VR(5),
    HOLOLENS(6),
    WINDOWS_10(7),
    WIN32(8),
    DEDICATED(9),
    TVOS(10),
    PLAYSTATION(11),
    NINTENDO(12),
    XBOX(13),
    WINDOWS_PHONE(14);

    companion object {
        private val VALUES = values()

        @JvmStatic
        fun from(findValue: Int) = VALUES.first { it.id == findValue }
    }
}
