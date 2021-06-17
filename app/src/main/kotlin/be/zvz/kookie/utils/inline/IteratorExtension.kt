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
package be.zvz.kookie.utils.inline

/**
 * This works the same as using two repeats.
 * @see kotlin.repeat
 */
inline fun repeat2(times: Int, action: (Int, Int) -> Unit) = repeat2(times, times, action)
inline fun repeat2(first: Int, second: Int, action: (Int, Int) -> Unit) {
    repeat(first) { i ->
        repeat(second) { j ->
            action(i, j)
        }
    }
}

/**
 * This works the same as using three repeats.
 * @see kotlin.repeat
 */
inline fun repeat3(times: Int, action: (Int, Int, Int) -> Unit) = repeat3(times, times, times, action)
inline fun repeat3(first: Int, second: Int, third: Int, action: (Int, Int, Int) -> Unit) {
    repeat2(first, second) { i, j ->
        repeat(third) { k ->
            action(i, j, k)
        }
    }
}
