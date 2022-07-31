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
package be.zvz.kookie.world.sound

import com.nukkitx.protocol.bedrock.data.SoundEvent

class RecordSound(val disk: Disk) : StandardSound(disk.type) {
    enum class Disk(val soundName: String, val type: SoundEvent, val translationKey: String) {
        ELEVEN("C418 - 11", SoundEvent.RECORD_11, "item.record_11.desc"),
        THIRTEEN("C418 - 13", SoundEvent.RECORD_13, "item.record_13.desc"),

        CAT("C418 - cat", SoundEvent.RECORD_CAT, "item.record_cat.desc"),
        BLOCKS("C418 - blocks", SoundEvent.RECORD_BLOCKS, "item.record_blocks.desc"),
        CHIRP("C418 - chirp", SoundEvent.RECORD_CHIRP, "item.record_chirp.desc"),
        FAR("C418 - far", SoundEvent.RECORD_FAR, "item.record_far.desc"),
        MALL("C418 - mall", SoundEvent.RECORD_MALL, "item.record_mall.desc"),
        MELLOHI("C418 - mellohi", SoundEvent.RECORD_MELLOHI, "item.record_mellohi.desc"),
        STAL("C418 - stal", SoundEvent.RECORD_STAL, "item.record_stal.desc"),
        STRAD("C418 - strad", SoundEvent.RECORD_STRAD, "item.record_strad.desc"),
        WAIT("C418 - ward", SoundEvent.RECORD_WARD, "item.record_ward.desc"),
        WARD("C418 - wait", SoundEvent.RECORD_WAIT, "item.record_wait.desc");
        // TODO: Lena Raine - Pig step

        companion object {
            private val VALUES = values()

            @JvmStatic fun from(findValue: String) = VALUES.first { it.soundName == findValue }
            @JvmStatic fun from(findValue: SoundEvent) = VALUES.first { it.type == findValue }
        }
    }
}
