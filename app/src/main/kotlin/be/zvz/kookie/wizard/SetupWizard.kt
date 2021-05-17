package be.zvz.kookie.wizard

import be.zvz.kookie.App
import be.zvz.kookie.Server
import be.zvz.kookie.VersionInfo
import be.zvz.kookie.lang.Language
import be.zvz.kookie.utils.Config
import be.zvz.kookie.utils.Internet
import org.slf4j.LoggerFactory
import java.net.InetAddress
import java.net.UnknownHostException
import java.nio.file.Path

class SetupWizard(private val cwd: Path, private val dataPath: Path, private val pluginPath: Path) {
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

        var langStr: String? = null
        do {
            langStr = getInput("Language", "eng").lowercase()
            if (langs.containsKey(langStr)) {
                logger.error("Couldn't find the language")
                langStr = null
            }
        } while (langStr === null)

        lang = Language(langStr)

        logger.info(lang.get("language_has_been_selected"))

        if (!showLicense()) {
            return false
        }

        val config = Config(
            dataPath.resolve(App.SERVER_PROPERTIES_NAME).toAbsolutePath().toString(),
            Config.Type.PROPERTIES
        )
        config.set("language", langStr)
        config.save()

        if (getInput(lang.get("skip_installer"), "n", "y/N") === "y") {
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
        logger.info(lang.get("you_have_finished"))
        logger.info(lang.get("pocketmine_plugins"))
        logger.info(lang.translateString("pocketmine_will_start", listOf(VersionInfo.NAME)))

        println()
        println()

        Thread.sleep(4000)

        Server(cwd, dataPath, pluginPath)
    }

    private fun networkFunctions() {
        val config = Config(
            dataPath.resolve(App.SERVER_PROPERTIES_NAME).toAbsolutePath().toString(),
            Config.Type.PROPERTIES
        )
        logger.warn(lang.get("query_warning1"))
        logger.warn(lang.get("query_warning2"))
        if (getInput(lang.get("query_disable"), "n", "y/N").equals("y", true)) {
            config.set("enable-query", false)
        } else {
            config.set("enable-query", true)
        }
        config.save()
    }

    private fun generateUserFiles() {
        logger.info(lang.get("op_info"))
        val op = getInput(lang.get("op_who"), "").lowercase()
        if (op.isEmpty()) {
            logger.warn(lang.get("op_warning"))
        } else {
            val ops = Config(dataPath.resolve("ops.txt").toAbsolutePath().toString(), Config.Type.ENUM)
            ops.set(op, true)
            ops.save()
        }
        logger.info(lang.get("whitelist_info"))

        val config = Config(
            dataPath.resolve(App.SERVER_PROPERTIES_NAME).toAbsolutePath().toString(),
            Config.Type.PROPERTIES
        )
        if (getInput(lang.get("whitelist_enable"), "n", "y/N").equals("y", true)) {
            logger.error(lang.get("whitelist_warning"))
            config.set("white-list", true)
        } else {
            config.set("white-list", false)
        }
        config.save()
    }

    private fun generateBaseConfig() {
        val config = Config(
            dataPath.resolve(App.SERVER_PROPERTIES_NAME).toAbsolutePath().toString(),
            Config.Type.PROPERTIES
        )
        val serverName = getInput(lang.get("name_your_server"), DEFAULT_NAME)
        config.set("motd", serverName)
        config.set("server-name", serverName)

        logger.warn(lang.get("port_warning"))

        var port: Int
        do {
            try {
                port = getInput(lang.get("server_port"), DEFAULT_PORT.toString()).toInt()
                if (port !in 0..65535) {
                    logger.error(lang.get("invalid_port"))
                    continue
                }
            } catch (e: NumberFormatException) {
                logger.error(lang.get("invalid_port"))
                continue
            }

            break
        } while (true)
        config.set("server-port", port)
        logger.info(lang.get("gamemode_info"))

        var gamemode: Int
        do {
            try {
                gamemode = getInput(lang.get("default_gamemode"), "0" /* TODO: GameMode Class */).toInt()
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
        logger.info(lang.get("setting_up_server_now"))
        logger.info(lang.get("default_values_info"))
        logger.info(lang.get("server_properties"))
    }

    private fun printIpDetails() {
        logger.info(lang.get("ip_get"))

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
                "ip_warning",
                mapOf(
                    "EXTERNAL_IP" to externalIp,
                    "INTERNAL_IP" to internalIp
                )
            )
        )
    }

    private fun showLicense(): Boolean {
        logger.info(lang.translateString("welcome_to_pocketmine", listOf(VersionInfo.NAME)))
        println(
            """
            LICENSE
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
            """.trimIndent()
        )
        return if (getInput("accept_license", "n", "y/N") !== "y") {
            logger.error(lang.translateString("you_have_to_accept_the_license", listOf(VersionInfo.NAME)))
            Thread.sleep(5000)

            false
        } else {
            true
        }
    }

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
        val DEFAULT_NAME = "${VersionInfo.NAME} Server"
        const val DEFAULT_PORT = 19132
        const val DEFAULT_PLAYERS = 20
    }
}
