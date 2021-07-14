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
package be.zvz.kookie.block.utils

import be.zvz.kookie.network.mcpe.protocol.LevelSoundEventPacket

enum class RecordType(val soundName: String, val soundId: Int, val translationKey: String) {
    DISK_13("C418 - 13", LevelSoundEventPacket.Sound.RECORD_13.sound, "item.record_13.desc"),
    DISK_CAT("C418 - cat", LevelSoundEventPacket.Sound.RECORD_CAT.sound, "item.record_cat.desc"),
    DISK_BLOCKS("C418 - blocks", LevelSoundEventPacket.Sound.RECORD_BLOCKS.sound, "item.record_blocks.desc"),
    DISK_CHIRP("C418 - chirp", LevelSoundEventPacket.Sound.RECORD_CHIRP.sound, "item.record_chirp.desc"),
    DISK_FAR("C418 - far", LevelSoundEventPacket.Sound.RECORD_FAR.sound, "item.record_far.desc"),
    DISK_MALL("C418 - mall", LevelSoundEventPacket.Sound.RECORD_MALL.sound, "item.record_mall.desc"),
    DISK_MELLOHI("C418 - mellohi", LevelSoundEventPacket.Sound.RECORD_MELLOHI.sound, "item.record_mellohi.desc"),
    DISK_STAL("C418 - stal", LevelSoundEventPacket.Sound.RECORD_STAL.sound, "item.record_stal.desc"),
    DISK_STRAD("C418 - strad", LevelSoundEventPacket.Sound.RECORD_STRAD.sound, "item.record_strad.desc"),
    DISK_WARD("C418 - ward", LevelSoundEventPacket.Sound.RECORD_WARD.sound, "item.record_ward.desc"),
    DISK_11("C418 - 11", LevelSoundEventPacket.Sound.RECORD_11.sound, "item.record_11.desc"),
    DISK_WAIT("C418 - wait", LevelSoundEventPacket.Sound.RECORD_WAIT.sound, "item.record_wait.desc"),
}
