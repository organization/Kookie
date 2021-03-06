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
package be.zvz.kookie.world.format.io

import be.zvz.kookie.world.WorldCreationOptions
import java.nio.file.Path

class WriteableWorldProviderManagerEntry(
    valid: (String) -> Boolean,
    val fromPath: (String) -> WritableWorldProvider,
    val generate: (String, String, WorldCreationOptions) -> Unit
) : WorldProviderManagerEntry(valid) {
    override fun fromPath(path: Path): WorldProvider {
        return fromPath(path.toString())
    }

    fun generate(path: Path, name: String, options: WorldCreationOptions) {
        generate(path.toString(), name, options)
    }
}
