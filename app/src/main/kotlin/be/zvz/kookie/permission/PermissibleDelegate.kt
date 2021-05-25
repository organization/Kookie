package be.zvz.kookie.permission

import be.zvz.kookie.plugin.Plugin

interface PermissibleDelegate : Permissible {
    val perm: PermissibleBase

    override val permissionRecalculationCallbacks: Set<(changedPermissionsOldValues: Map<String, Boolean>) -> Unit>
        get() = perm.permissionRecalculationCallbacks

    override fun setBasePermission(name: String, grant: Boolean) {
        perm.setBasePermission(name, grant)
    }

    override fun unsetBasePermission(name: String) {
        perm.unsetBasePermission(name)
    }

    override fun isPermissionSet(name: String): Boolean = perm.isPermissionSet(name)
    override fun hasPermission(name: String): Boolean = perm.hasPermission(name)
    override fun addAttachment(plugin: Plugin, name: String?, value: Boolean?): PermissionAttachment =
        perm.addAttachment(plugin, name, value)

    override fun removeAttachment(attachment: PermissionAttachment) {
        perm.removeAttachment(attachment)
    }

    override fun recalculatePermissions(): Map<String, Boolean> = perm.recalculatePermissions()
    override fun getEffectivePermissions(): Map<String, PermissionAttachmentInfo> = perm.getEffectivePermissions()
}
