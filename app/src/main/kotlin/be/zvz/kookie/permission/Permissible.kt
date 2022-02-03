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

import be.zvz.kookie.plugin.Plugin

interface Permissible {
    val permissionRecalculationCallbacks: Set<(changedPermissionsOldValues: Map<String, Boolean>) -> Unit>

    fun setBasePermission(permission: Permission, grant: Boolean) {
        setBasePermission(permission.name, grant)
    }

    fun setBasePermission(name: String, grant: Boolean)

    fun unsetBasePermission(permission: Permission) {
        unsetBasePermission(permission.name)
    }

    fun unsetBasePermission(name: String)

    fun isPermissionSet(permission: Permission): Boolean = isPermissionSet(permission.name)
    fun isPermissionSet(name: String): Boolean

    fun hasPermission(permission: Permission): Boolean = hasPermission(permission.name)
    fun hasPermission(name: String): Boolean

    fun addAttachment(plugin: Plugin, name: String? = null, value: Boolean? = null): PermissionAttachment

    fun removeAttachment(attachment: PermissionAttachment)

    fun recalculatePermissions(): Map<String, Boolean>

    fun getEffectivePermissions(): Map<String, PermissionAttachmentInfo>
}
