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
package be.zvz.kookie.world.sound

class RecordSound(val disk: Disk) : StandardSound(disk.type) {
    enum class Disk(val soundName: String, val type: Type, val translationKey: String) {
        ELEVEN("C418 - 11", Type.RECORD_11, "item.record_11.desc"),
        THIRTEEN("C418 - 13", Type.RECORD_13, "item.record_13.desc"),

        CAT("C418 - cat", Type.RECORD_CAT, "item.record_cat.desc"),
        BLOCKS("C418 - blocks", Type.RECORD_BLOCKS, "item.record_blocks.desc"),
        CHIRP("C418 - chirp", Type.RECORD_CHIRP, "item.record_chirp.desc"),
        FAR("C418 - far", Type.RECORD_FAR, "item.record_far.desc"),
        MALL("C418 - mall", Type.RECORD_MALL, "item.record_mall.desc"),
        MELLOHI("C418 - mellohi", Type.RECORD_MELLOHI, "item.record_mellohi.desc"),
        STAL("C418 - stal", Type.RECORD_STAL, "item.record_stal.desc"),
        STRAD("C418 - strad", Type.RECORD_STRAD, "item.record_strad.desc"),
        WAIT("C418 - ward", Type.RECORD_WARD, "item.record_ward.desc"),
        WARD("C418 - wait", Type.RECORD_WAIT, "item.record_wait.desc");
        // TODO: Lena Raine - Pig step

        companion object {
            private val VALUES = values()

            @JvmStatic fun from(findValue: String) = VALUES.first { it.soundName == findValue }
            @JvmStatic fun from(findValue: Type) = VALUES.first { it.type == findValue }
            @JvmStatic fun from(findValue: Int) = VALUES.first { it.type.id == findValue }
        }
    }
}
