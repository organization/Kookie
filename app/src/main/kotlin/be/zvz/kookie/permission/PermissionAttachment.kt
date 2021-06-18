/**
 *
 * _  __           _    _
 * | |/ /___   ___ | | _(_) ___
 * | ' // _ \ / _ \| |/ / |/ _ \
 * | . \ (_) | (_) |   <| |  __/
 * |_|\_\___/ \___/|_|\_\_|\___|
 *
 * A server software for Minecraft: Bedrock Edition
 *
 * Copyright (C) 2021 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.permission

import be.zvz.kookie.plugin.Plugin
import be.zvz.kookie.plugin.PluginException
import be.zvz.kookie.utils.inline.forEachValue
import com.koloboke.collect.map.hash.HashIntObjMaps
import com.koloboke.collect.map.hash.HashObjObjMaps

class PermissionAttachment(val plugin: Plugin) {
    init {
        if (!plugin.enabled) {
            throw PluginException("Plugin ${plugin.description.name} is disabled")
        }
    }

    var removalCallback: PermissionRemovedExecutor? = null

    val permissions: MutableMap<String, Boolean> = HashObjObjMaps.newMutableMap()

    val subscribers: MutableMap<Int, Permissible> = HashIntObjMaps.newMutableMap()

    private fun recalculatePermissibles() {
        subscribers.forEachValue { value ->
            value.recalculatePermissions()
        }
    }

    fun clearPermissions() {
        permissions.clear()
        recalculatePermissibles()
    }

    fun setPermissions(permissions: Map<String, Boolean>) {
        permissions.forEach(this.permissions::put)
        recalculatePermissibles()
    }

    fun unsetPermissions(permissions: List<String>) {
        permissions.forEach(this.permissions::remove)
        recalculatePermissibles()
    }

    fun setPermission(permission: Permission, value: Boolean) = setPermission(permission.name, value)
    fun setPermission(name: String, value: Boolean) {
        if (permissions[name] != null) {
            if (permissions[name] == value) {
                return
            }

            permissions.remove(name)
        }

        permissions[name] = value
        recalculatePermissibles()
    }

    fun unsetPermission(permission: Permission) = unsetPermission(permission.name)
    fun unsetPermission(name: String) {
        if (permissions[name] != null) {
            permissions.remove(name)
            recalculatePermissibles()
        }
    }

    fun subscribePermissible(permissible: Permissible) {
        subscribers[permissible.hashCode()] = permissible
    }

    fun unsubscribePermissible(permissible: Permissible) {
        subscribers.remove(permissible.hashCode())
    }
}
