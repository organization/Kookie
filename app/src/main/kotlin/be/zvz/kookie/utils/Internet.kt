package be.zvz.kookie.utils

import org.apache.commons.io.IOUtils
import java.net.URL

object Internet {
    var ip = ""
    var online = true
    @JvmOverloads
    fun getIP(force: Boolean = false): String {
        if (!online) {
            return ""
        } else if (ip.isNotEmpty() && !force) {
            return ip
        }

        val connection = URL("https://checkip.amazonaws.com")
            .openConnection()
        connection.getInputStream().use {
            val encoding = connection.contentEncoding ?: "UTF-8"
            return IOUtils.toString(it, encoding).trim()
        }
    }
}
