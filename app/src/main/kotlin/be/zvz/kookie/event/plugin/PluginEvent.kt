package be.zvz.kookie.event.plugin

import be.zvz.kookie.event.Event
import be.zvz.kookie.plugin.Plugin

abstract class PluginEvent(val plugin: Plugin) : Event()
