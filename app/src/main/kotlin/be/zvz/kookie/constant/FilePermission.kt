package be.zvz.kookie.constant

import java.nio.file.attribute.PosixFilePermission

object FilePermission {
    val perm777 = setOf(
        PosixFilePermission.GROUP_EXECUTE,
        PosixFilePermission.GROUP_READ,
        PosixFilePermission.GROUP_WRITE,
        PosixFilePermission.OTHERS_EXECUTE,
        PosixFilePermission.OTHERS_READ,
        PosixFilePermission.OTHERS_WRITE,
        PosixFilePermission.OWNER_EXECUTE,
        PosixFilePermission.OWNER_WRITE,
        PosixFilePermission.OWNER_READ
    )
}