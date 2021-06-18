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
package be.zvz.kookie.command

import be.zvz.kookie.Server
import be.zvz.kookie.lang.Language
import be.zvz.kookie.lang.TranslationContainer
import be.zvz.kookie.permission.DefaultPermissions
import be.zvz.kookie.permission.PermissibleBase
import be.zvz.kookie.permission.PermissibleDelegate
import org.slf4j.LoggerFactory

open class ConsoleCommandSender(
    override val server: Server,
    override val language: Language,
) : PermissibleDelegate, CommandSender {
    private val logger = LoggerFactory.getLogger(this::class.java)
    override val name: String = "CONSOLE"
    protected var lineHeight: Int? = null
    override val perm = PermissibleBase(
        mapOf(
            DefaultPermissions.ROOT_CONSOLE to true
        )
    )

    override fun sendMessage(message: TranslationContainer) = sendRawMessage(language.translate(message))
    override fun sendMessage(message: String) = sendRawMessage(language.translateString(message))

    private fun sendRawMessage(text: String) {
        text.split("\n").forEach(logger::info)
    }

    override fun getScreenLineHeight(): Int = lineHeight ?: Int.MAX_VALUE
    override fun setScreenLineHeight(height: Int?) {
        if (height != null && height < 1) {
            throw IllegalArgumentException("Line height must be at least 1")
        }

        lineHeight = height
    }
}
