package be.zvz.kookie.event.plugin

import be.zvz.kookie.event.Event
import be.zvz.kookie.plugin.Plugin

open class PluginEvent(val plugin: Plugin) : Event()
