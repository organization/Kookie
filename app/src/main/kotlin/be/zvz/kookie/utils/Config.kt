package be.zvz.kookie.utils

import org.apache.commons.io.FilenameUtils
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.inputStream

class Config(private val file: String, private var type: Type = Type.DETECT, default: ConfigBrowser) {
    enum class Type {
        DETECT,
        PROPERTIES,
        JSON,
        YAML,
        ENUM,
        ERROR
    }
    private var changed = false
    private var config: ConfigBrowser = ConfigBrowser.NULL_BROWSER

    init {
        load(default)
    }

    private fun load(default: ConfigBrowser) {
        if (type === Type.DETECT) {
            val extension = FilenameUtils.getExtension(file)
            val format = getFormat(extension)
            if (format !== Type.ERROR) {
                type = format
            } else {
                throw IllegalArgumentException("Cannot detect config type of $file")
            }
        }

        val filePath = Paths.get(file)
        config = if (!filePath.exists()) {
            default
        } else {
            filePath.inputStream().use { inputStream ->
                BufferedInputStream(inputStream).use {
                    when (type) {
                        Type.PROPERTIES -> PropertiesBrowser().parse(it)
                        Type.JSON -> JsonBrowser().parse(it)
                        Type.YAML -> YAMLBrowser().parse(it)
                        Type.ENUM -> parseList(it)
                        else -> throw IllegalStateException("Config type is unknown")
                    }
                }
            }
        }
        if (fillDefault(default) > 0) {
            save()
        }
    }

    private fun save() {
        TODO("Not yet implemented")
    }

    private fun fillDefault(default: ConfigBrowser): Int {
        TODO("Not yet implemented")
    }

    private fun parseList(inputStream: InputStream): ConfigBrowser = JsonBrowser().apply {
        inputStream.bufferedReader().use(BufferedReader::readText)
            .replace("\r\n", "\n").trim().split('\n')
            .forEach {
                val v = it.trim()
                if (v.isEmpty()) {
                    return@forEach
                }
                put(v, true)
            }
    }

    companion object {
        fun getFormat(type: String) = when (type) {
            "properties", "cnf", "conf", "config" -> Type.PROPERTIES
            "json", "js" -> Type.JSON
            "yml", "yaml" -> Type.YAML
            // "export", "xport" -> EXPORT
            // "sl", "serialize" -> SERIALIZED
            "txt", "list", "enum" -> Type.ENUM
            else -> Type.ERROR
        }
    }
}
