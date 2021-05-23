package be.zvz.kookie.plugin

import be.zvz.kookie.utils.ServerException

open class PluginException(message: String = "Plugin exception") : ServerException(message)