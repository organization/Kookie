package be.zvz.kookie.permission

interface PermissionRemovedExecutor {
    fun attachmentRemoved(permissible: Permissible, attachment: PermissionAttachment)
}