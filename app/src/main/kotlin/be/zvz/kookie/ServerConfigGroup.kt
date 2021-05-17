package be.zvz.kookie

import be.zvz.kookie.utils.Config
import be.zvz.kookie.utils.ConfigBrowser
import com.koloboke.collect.map.hash.HashObjObjMaps

class ServerConfigGroup(
    private val pocketmineYml: Config,
    private val serverProperties: Config
) {

    private val propertyCache: MutableMap<String, ConfigBrowser> = HashObjObjMaps.newMutableMap()

    @JvmOverloads
    fun getProperty(
        variable: String,
        defaultValue: ConfigBrowser = ConfigBrowser.NULL_BROWSER
    ): ConfigBrowser {
        if (!propertyCache.containsKey(variable)) {
            propertyCache[variable] = pocketmineYml.get(variable)
        }

        return propertyCache[variable] ?: defaultValue
    }

    @JvmOverloads
    fun getConfigString(variable: String, defaultValue: String = ""): String = if (serverProperties.exists(variable)) {
        serverProperties.get(variable).safeText()
    } else {
        defaultValue
    }

    fun setConfigString(variable: String, value: String) = serverProperties.set(variable, value)

    @JvmOverloads
    fun getConfigLong(variable: String, defaultValue: Long = 0): Long =
        serverProperties.get(variable).asLong(defaultValue)

    fun setConfigLong(variable: String, value: Long) =
        serverProperties.set(variable, value)

    @JvmOverloads
    fun getConfigBool(variable: String, defaultValue: Boolean = false): Boolean =
        serverProperties.get(variable).asBoolean(defaultValue)

    fun setConfigBool(variable: String, value: Boolean) =
        serverProperties.set(variable, value)

    fun save() {
        if (serverProperties.hasChanged()) {
            serverProperties.save()
        }
        if (pocketmineYml.hasChanged()) {
            pocketmineYml.save()
        }
    }
}
