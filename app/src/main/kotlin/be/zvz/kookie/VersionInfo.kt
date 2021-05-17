package be.zvz.kookie

import java.io.IOException
import java.util.*

object VersionInfo {
    const val NAME = "Kookie"
    const val IS_DEVELOPMENT_BUILD = true
    const val BUILD_NUMBER = 0
    private val gitPrefs = Properties().apply {
        try {
            load(this::class.java.getResourceAsStream("git.properties"))
        } catch (ignored: IOException) {}
    }
    val GIT_HASH = gitPrefs.getProperty("git.commit.id") ?: ""
    val GIT_IS_DIRTY = gitPrefs.getProperty("git.commit.id") ?: true
    val BASE_VERSION = gitPrefs.getProperty("git.build.version") ?: when {
        this::class.java.getPackage() != null && this::class.java.getPackage().implementationVersion != null ->
            this::class.java.getPackage().implementationVersion
        else -> "UNKNOWN"
    }
}
