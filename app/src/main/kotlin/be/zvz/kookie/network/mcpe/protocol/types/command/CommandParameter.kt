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
package be.zvz.kookie.network.mcpe.protocol.types.command

import be.zvz.kookie.network.mcpe.protocol.AvailableCommandsPacket

class CommandParameter @JvmOverloads constructor(
    var paramName: String = "",
    var paramType: Int = -1,
    var isOptional: Boolean = false,
    var flags: Int = -1,
    var enum: CommandEnum? = null,
    var postfix: String? = null
) {

    companion object {
        const val FLAG_FORCE_COLLAPSE_ENUM = 0x1
        const val FLAG_HAS_ENUM_CONSTRAINT = 0x2

        private fun baseline(name: String, type: Int, flags: Int, optional: Boolean): CommandParameter =
            CommandParameter(name, type, optional, flags, null, null)

        @JvmStatic
        fun standard(name: String, type: Int, flags: Int, optional: Boolean = false): CommandParameter =
            baseline(name, AvailableCommandsPacket.ARG_FLAG_VALID or type, flags, optional)

        @JvmStatic
        fun postfixed(name: String, postfix: String, flags: Int, optional: Boolean = false): CommandParameter =
            baseline(name, AvailableCommandsPacket.ARG_FLAG_POSTFIX, flags, optional).apply {
                this.postfix = postfix
            }
    }
}
