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
package be.zvz.kookie.lang

import be.zvz.kookie.utils.Union

class TranslationContainer(
    val text: String,
    array: List<Union.U3<String, Float, Int>>,
) {
    val params: List<String>

    init {
        params = mutableListOf<String>().apply {
            array.forEach { raw ->
                raw.use(
                    this::add,
                    { add(it.toString()) },
                    { add(it.toString()) },
                )
            }
        }
    }

    fun getParameter(index: Int): String = params[index]
}
