package be.zvz.kookie.permission

data class PermissionAttachmentInfo(
    val permission: String,
    val attachment: PermissionAttachment?,
    val value: Boolean,
    val groupPermission: PermissionAttachmentInfo?
)
