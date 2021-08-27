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
package be.zvz.kookie.wizard

import be.zvz.kookie.App
import be.zvz.kookie.VersionInfo
import be.zvz.kookie.lang.KnownTranslationKeys
import be.zvz.kookie.lang.Language
import be.zvz.kookie.utils.Config
import be.zvz.kookie.utils.Internet
import org.slf4j.LoggerFactory
import java.net.InetAddress
import java.net.UnknownHostException
import java.nio.file.Path

class SetupWizard(private val dataPath: Path) {
    private val logger = LoggerFactory.getLogger(SetupWizard::class.java)
    private lateinit var lang: Language

    fun run(): Boolean {
        logger.info("${VersionInfo.NAME} set-up wizard")

        val langs = try {
            Language.getLanguageList()
        } catch (e: Language.LanguageNotFoundException) {
            logger.error("No language files found, please use provided builds or clone the repository recursively.")
            return false
        }

        logger.info("Please select a language")
        langs.forEach { (short, native) ->
            println("[*] $native => $short")
        }

        var langStr: String?
        do {
            langStr = getInput("Language", "eng").lowercase()
            if (!langs.containsKey(langStr)) {
                logger.error("Couldn't find the language")
                langStr = null
            }
        } while (langStr === null)

        lang = Language(langStr)

        logger.info(lang.get(KnownTranslationKeys.LANGUAGE_HAS_BEEN_SELECTED))

        if (!showLicense()) {
            return false
        }

        val config = Config(
            dataPath.resolve(App.SERVER_PROPERTIES_NAME),
            Config.Type.PROPERTIES
        )
        config.set("language", langStr)
        config.save()

        if (getInput(lang.get(KnownTranslationKeys.SKIP_INSTALLER), "n", "y/N") == "y") {
            printIpDetails()
            return true
        }

        println()
        welcome()
        generateBaseConfig()
        generateUserFiles()

        networkFunctions()
        printIpDetails()

        endWizard()

        return true
    }

    private fun endWizard() {
        logger.info(lang.get(KnownTranslationKeys.YOU_HAVE_FINISHED))
        logger.info(lang.get(KnownTranslationKeys.KOOKIE_PLUGINS))
        logger.info(lang.translateString(KnownTranslationKeys.KOOKIE_WILL_START, listOf(VersionInfo.NAME)))

        println()
        println()

        Thread.sleep(4000)
    }

    private fun networkFunctions() {
        val config = Config(
            dataPath.resolve(App.SERVER_PROPERTIES_NAME),
            Config.Type.PROPERTIES
        )
        logger.warn(lang.get(KnownTranslationKeys.QUERY_WARNING1))
        logger.warn(lang.get(KnownTranslationKeys.QUERY_WARNING2))
        if (getInput(lang.get(KnownTranslationKeys.QUERY_DISABLE), "n", "y/N").equals("y", true)) {
            config.set("enable-query", false)
        } else {
            config.set("enable-query", true)
        }
        config.save()
    }

    private fun generateUserFiles() {
        logger.info(lang.get(KnownTranslationKeys.OP_INFO))
        val op = getInput(lang.get(KnownTranslationKeys.OP_WHO), "").lowercase()
        if (op.isEmpty()) {
            logger.warn(lang.get(KnownTranslationKeys.OP_WARNING))
        } else {
            val ops = Config(dataPath.resolve("ops.txt"), Config.Type.ENUM)
            ops.set(op, true)
            ops.save()
        }
        logger.info(lang.get(KnownTranslationKeys.WHITELIST_INFO))

        val config = Config(
            dataPath.resolve(App.SERVER_PROPERTIES_NAME),
            Config.Type.PROPERTIES
        )
        if (getInput(lang.get(KnownTranslationKeys.WHITELIST_ENABLE), "n", "y/N").equals("y", true)) {
            logger.error(lang.get(KnownTranslationKeys.WHITELIST_WARNING))
            config.set("white-list", true)
        } else {
            config.set("white-list", false)
        }
        config.save()
    }

    private fun generateBaseConfig() {
        val config = Config(
            dataPath.resolve(App.SERVER_PROPERTIES_NAME),
            Config.Type.PROPERTIES
        )
        val serverName = getInput(lang.get(KnownTranslationKeys.NAME_YOUR_SERVER), DEFAULT_NAME)
        config.set("motd", serverName)
        config.set("server-name", serverName)

        logger.warn(lang.get(KnownTranslationKeys.PORT_WARNING))

        var port: Int
        do {
            try {
                port = getInput(lang.get(KnownTranslationKeys.SERVER_PORT), DEFAULT_PORT.toString()).toInt()
                if (port !in 0..65535) {
                    logger.error(lang.get(KnownTranslationKeys.INVALID_PORT))
                    continue
                }
            } catch (e: NumberFormatException) {
                logger.error(lang.get(KnownTranslationKeys.INVALID_PORT))
                continue
            }

            break
        } while (true)
        config.set("server-port", port)
        logger.info(lang.get(KnownTranslationKeys.GAMEMODE_INFO))

        var gamemode: Int
        do {
            try {
                gamemode = getInput(lang.get(KnownTranslationKeys.DEFAULT_GAMEMODE), "0" /* TODO: GameMode Class */).toInt()
            } catch (e: NumberFormatException) {
                gamemode = -1
                continue
            }
        } while (gamemode < 0 || gamemode > 3)
        config.set("gamemode", gamemode)
        config.set("max-players", getInput(lang.get("max-players"), DEFAULT_PLAYERS.toString()).toInt())
        config.save()
    }

    private fun welcome() {
        logger.info(lang.get(KnownTranslationKeys.SETTING_UP_SERVER_NOW))
        logger.info(lang.get(KnownTranslationKeys.DEFAULT_VALUES_INFO))
        logger.info(lang.get(KnownTranslationKeys.SERVER_PROPERTIES))
    }

    private fun printIpDetails() {
        logger.info(lang.get(KnownTranslationKeys.IP_GET))

        var externalIp = Internet.getIP()
        if (externalIp.isEmpty()) {
            externalIp = "unknown (server offline)"
        }
        val internalIp = try {
            InetAddress.getLocalHost().hostAddress.toString()
        } catch (e: UnknownHostException) {
            "unknown (${e.message})"
        }

        logger.error(
            lang.translateString(
                KnownTranslationKeys.IP_WARNING,
                mapOf(
                    "EXTERNAL_IP" to externalIp,
                    "INTERNAL_IP" to internalIp
                )
            )
        )
    }

    private fun showLicense(): Boolean {
        logger.info(lang.translateString(KnownTranslationKeys.WELCOME_TO_POCKETMINE, listOf(VersionInfo.NAME)))
        println(
            """
            LICENSE
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
            """.trimIndent()
        )
        return if (getInput(lang.translateString(KnownTranslationKeys.ACCEPT_LICENSE), "n", "y/N") != "y") {
            logger.error(lang.translateString(KnownTranslationKeys.YOU_HAVE_TO_ACCEPT_THE_LICENSE, listOf(VersionInfo.NAME)))
            Thread.sleep(5000)

            false
        } else {
            true
        }
    }

    @JvmOverloads
    fun getInput(msg: String, default: String = "", options: String = ""): String {
        val message = StringBuilder("[?] $msg")

        if (options.isNotEmpty() || default.isNotEmpty()) {
            message.append(" (")
                .append(
                    options.ifEmpty {
                        default
                    }
                )
                .append(")")
        }
        message.append(": ")

        print(message)
        val input = readLine()
        return if (input !== null && input.isNotEmpty()) {
            input
        } else {
            default
        }
    }

    companion object {
        const val DEFAULT_NAME = "${VersionInfo.NAME} Server"
        const val DEFAULT_PORT = 19132
        const val DEFAULT_PLAYERS = 20
    }
}
