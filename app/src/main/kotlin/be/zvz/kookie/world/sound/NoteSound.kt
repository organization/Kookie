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

import com.nukkitx.protocol.bedrock.data.SoundEvent

class NoteSound(val instrument: Instrument, val note: Int) : StandardSound(SoundEvent.NOTE) {
    init {
        if (note !in 0 until 0xff) {
            throw IllegalArgumentException("Note $note is outside accepted range")
        }
    }

    override val extraData: Int get() = instrument.id shl 8 or note

    enum class Instrument(val id: Int) {
        PIANO(0),
        BASS_DRUM(1),
        SNARE(2),
        CLICKS_AND_STICKS(3),
        DOUBLE_BASS(4);

        companion object {
            private val VALUES = values()

            @JvmStatic fun from(findValue: Int) = VALUES.first { it.id == findValue }
        }
    }
}
