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

import com.koloboke.collect.map.hash.HashObjObjMaps

object PermissionManager {
    private val permissions: MutableMap<String, Permission> = HashObjObjMaps.newMutableMap()
    val permSubs: MutableMap<String, MutableMap<String, Permissible>> = HashObjObjMaps.newMutableMap()

    @JvmStatic
    fun getPermission(name: String): Permission? = permissions[name]

    @JvmStatic
    fun addPermission(permission: Permission): Boolean = when (permissions[permission.name]) {
        null -> false
        else -> {
            permissions[permission.name] = permission

            true
        }
    }

    @JvmStatic fun removePermission(permission: Permission) = removePermission(permission.name)
    @JvmStatic fun removePermission(name: String) = permissions.remove(name)

    @JvmStatic
    fun subscribeToPermission(permission: String, permissible: Permissible) {
        val a = permSubs[permission]
        // ["test"] = permissible

        a?.set(permissible.toString(), permissible)
    }

    @JvmStatic
    fun unsubscribeFromPermission(permission: String, permissible: Permissible) {
        if (this.permSubs.containsKey(permission)) {
            val perm = this.permSubs.getValue(permission)
            perm.remove(permissible.toString())
            if (perm.isEmpty()) {
                this.permSubs.remove(permission)
            }
        }
    }

    @JvmStatic
    fun unsubscribeFromAllPermissions(permissible: Permissible) {
        permSubs.forEach { (permission, subs) ->
            subs.remove(permissible.toString())
            if (subs.isEmpty()) {
                this.permSubs.remove(permission)
            }
        }
    }

    @JvmStatic
    fun getPermissionSubscriptions(permission: String): Map<String, Permissible> = this.permSubs[permission] ?: mapOf()

    @JvmStatic
    fun getPermissions(): Map<String, Permission> = this.permissions

    @JvmStatic
    fun clearPermissions() {
        this.permissions.clear()
    }
}
