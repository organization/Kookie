package be.zvz.kookie.command

import be.zvz.kookie.Server
import be.zvz.kookie.lang.Language
import be.zvz.kookie.lang.TranslationContainer

open class ConsoleCommandSender(
    override val server: Server,
    override val language: Language,
) : CommandSender {
    override val name: String = "CONSOLE"
    protected var lineHeight: Int? = null
    val perm: PermissibleBase(
        mapOf(
            DefaultPermissions.ROOT_CONSOLE to true
        )
    )

    override fun sendMessage(translation: TranslationContainer) = sendRawMessage(language.translate(translation))
    override fun sendMessage(message: String) = sendRawMessage(language.translateString(message))

    private fun sendRawMessage(text: String) {
        text.split("\n").forEach {
            server.logger.info(it)
        }
    }

    override fun getScreenLineHeight(): Int = lineHeight ?: Int.MAX_VALUE
    override fun setScreenLineHeight(height: Int?) {
        if (height != null && height < 1) {
            throw IllegalArgumentException("Line height must be at least 1")
        }

        lineHeight = height
    }
}