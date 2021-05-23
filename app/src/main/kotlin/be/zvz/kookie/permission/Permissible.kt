package be.zvz.kookie.permission

import be.zvz.kookie.plugin.Plugin

interface Permissible {
    fun setBasePermission(name: String, grant: Boolean)
    fun setBasePermission(permission: Permission, grant: Boolean)

    fun unsetBasePermission(name: String)
    fun unsetBasePermission(permission: Permission)

    fun isPermissionSet(name: String): Boolean
    fun isPermissionSet(permission: Permission): Boolean

    fun hasPermission(name: String): Boolean
    fun hasPermission(permission: Permission): Boolean

    fun addAttachment(plugin: Plugin, name: String? = null, value: Boolean? = null): PermissionAttachment

    fun removeAttachment(attachment: PermissionAttachment)

    fun recalculatePermissions(): Map<String, Boolean>

    fun getPermissionRecalculationCallbacks(): Set<(changedPermissionsOldValues: Map<String, Boolean>) -> Unit>

    fun getEffectivePermissions(): List<PermissionAttachmentInfo>
}
