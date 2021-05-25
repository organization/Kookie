package be.zvz.kookie.permission

import be.zvz.kookie.plugin.Plugin
import be.zvz.kookie.plugin.PluginException
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
        subscribers.forEach { (_, value) ->
            value.recalculatePermissions()
        }
    }

    fun clearPermissions() {
        permissions.clear()
        recalculatePermissibles()
    }

    fun setPermissions(permissions: Map<String, Boolean>) {
        permissions.forEach { (key, value) ->
            this.permissions[key] = value
        }
        recalculatePermissibles()
    }

    fun unsetPermissions(permissions: List<String>) {
        permissions.forEach {
            this.permissions.remove(it)
        }
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
