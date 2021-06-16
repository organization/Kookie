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
package be.zvz.kookie.network.mcpe.protocol

class PacketDecodeException @JvmOverloads internal constructor(
    msg: String,
    cause: Throwable? = null
) : RuntimeException(msg, cause) {
    companion object {
        @JvmStatic
        @JvmOverloads
        fun wrap(previous: Throwable, prefix: String? = null) = PacketDecodeException(
            if (prefix !== null) {
                "$prefix: "
            } else {
                ""
            } + previous.message,
            previous
        )
    }
}
