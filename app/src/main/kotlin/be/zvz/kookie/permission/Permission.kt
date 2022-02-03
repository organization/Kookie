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
 * Copyright (C) 2021 - 2022 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.permission

import com.koloboke.collect.map.hash.HashObjObjMaps

class Permission @JvmOverloads constructor(
    val name: String,
    var description: String? = null,
    val children: MutableMap<String, Boolean> = HashObjObjMaps.newMutableMap()
) {
    fun getPermissibles(): Map<String, Permissible> = PermissionManager.getPermissionSubscriptions(name)

    fun recalculatePermissibles() {
        getPermissibles().forEach {
            TODO("it.recalculatePermissions()")
        }
    }

    fun addChild(name: String, value: Boolean) {
        children[name] = value
        recalculatePermissibles()
    }

    fun removeChild(name: String) {
        children.remove(name)
        recalculatePermissibles()
    }
}
