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
        text.split("\n").forEach {
            logger.info(it)
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
