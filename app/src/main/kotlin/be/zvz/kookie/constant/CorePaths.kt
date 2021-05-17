package be.zvz.kookie.constant

import java.nio.file.Paths

object CorePaths {
    val PATH = Paths.get("").toAbsolutePath()
    val RESOURCE_PATH = PATH.resolve("resources")
}
