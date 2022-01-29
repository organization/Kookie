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
package be.zvz.kookie.block.util

import com.nukkitx.protocol.bedrock.data.SoundEvent

enum class RecordType(val soundName: String, val soundId: SoundEvent, val translationKey: String) {
    DISK_13("C418 - 13", SoundEvent.RECORD_13, "item.record_13.desc"),
    DISK_CAT("C418 - cat", SoundEvent.RECORD_CAT, "item.record_cat.desc"),
    DISK_BLOCKS("C418 - blocks", SoundEvent.RECORD_BLOCKS, "item.record_blocks.desc"),
    DISK_CHIRP("C418 - chirp", SoundEvent.RECORD_CHIRP, "item.record_chirp.desc"),
    DISK_FAR("C418 - far", SoundEvent.RECORD_FAR, "item.record_far.desc"),
    DISK_MALL("C418 - mall", SoundEvent.RECORD_MALL, "item.record_mall.desc"),
    DISK_MELLOHI("C418 - mellohi", SoundEvent.RECORD_MELLOHI, "item.record_mellohi.desc"),
    DISK_STAL("C418 - stal", SoundEvent.RECORD_STAL, "item.record_stal.desc"),
    DISK_STRAD("C418 - strad", SoundEvent.RECORD_STRAD, "item.record_strad.desc"),
    DISK_WARD("C418 - ward", SoundEvent.RECORD_WARD, "item.record_ward.desc"),
    DISK_11("C418 - 11", SoundEvent.RECORD_11, "item.record_11.desc"),
    DISK_WAIT("C418 - wait", SoundEvent.RECORD_WAIT, "item.record_wait.desc"),
}
