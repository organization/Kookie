package be.zvz.kookie.plugin

// TODO: Plugin
abstract class Plugin {
    var enabled: Boolean = false
    val description: PluginDescription = PluginDescription()
}
