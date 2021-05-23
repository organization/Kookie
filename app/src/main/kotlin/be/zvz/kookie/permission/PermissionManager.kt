package be.zvz.kookie.permission

import com.koloboke.collect.map.hash.HashObjObjMaps

object PermissionManager {
    private val permissions: MutableMap<String, Permission> = HashObjObjMaps.newMutableMap()
    val permSubs: MutableMap<String, MutableMap<String, Permissible>> = HashObjObjMaps.newMutableMap()

    fun getPermission(name: String): Permission? = permissions[name]

    fun addPermission(permission: Permission): Boolean = when (permissions[permission.name]) {
        null -> false
        else -> {
            permissions[permission.name] = permission

            true
        }
    }

    fun removePermission(permission: Permission) = removePermission(permission.name)
    fun removePermission(name: String) {
        permissions.remove(name)
    }

    fun subscribeToPermission(permission: String, permissible: Permissible) {
        val a = permSubs[permission]
        // ["test"] = permissible

        a?.set(permissible.toString(), permissible)
    }

    fun unsubscribeFromPermission(permission: String, permissible: Permissible) {
        if (this.permSubs.containsKey(permission)) {
            val perm = this.permSubs.getValue(permission)
            perm.remove(permissible.toString())
            if (perm.isEmpty()) {
                this.permSubs.remove(permission)
            }
        }
    }

    fun unsubscribeFromAllPermissions(permissible: Permissible) {
        permSubs.forEach { (permission, subs) ->
            subs.remove(permissible.toString())
            if (subs.isEmpty()) {
                this.permSubs.remove(permission)
            }
        }
    }

    fun getPermissionSubscriptions(permission: String): Map<String, Permissible> {
        return this.permSubs[permission] ?: mapOf()
    }

    fun getPermissions(): Map<String, Permission> {
        return this.permissions
    }

    fun clearPermissions() {
        this.permissions.clear()
    }
}
