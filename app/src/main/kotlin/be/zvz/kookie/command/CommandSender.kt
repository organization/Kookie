package be.zvz.kookie.command

import be.zvz.kookie.Server
import be.zvz.kookie.lang.Language
import be.zvz.kookie.lang.TranslationContainer
import be.zvz.kookie.permission.Permissible

interface CommandSender : Permissible { // TODO: CommandSender should implement Permissible
    val server: Server
    val language: Language
    val name: String

    fun sendMessage(message: String)
    fun sendMessage(message: TranslationContainer)

    fun getScreenLineHeight(): Int
    fun setScreenLineHeight(height: Int?)
}
