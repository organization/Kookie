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
package be.zvz.kookie.world.format.io

import be.zvz.kookie.world.WorldException
import be.zvz.kookie.world.format.io.exception.CorruptedWorldException
import be.zvz.kookie.world.format.io.exception.UnsupportedWorldFormatException
import java.nio.file.Path
import kotlin.io.path.exists

abstract class BaseWorldProvider(final override val path: Path) : WorldProvider {
    final override val worldData: WorldData = this.loadLevelData()

    init {
        if (!this.path.exists()) {
            throw WorldException("World does not exist")
        }
    }

    @Throws(CorruptedWorldException::class, UnsupportedWorldFormatException::class)
    protected abstract fun loadLevelData(): WorldData
}
