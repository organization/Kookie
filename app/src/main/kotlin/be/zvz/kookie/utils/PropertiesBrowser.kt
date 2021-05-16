package be.zvz.kookie.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsFactory

class PropertiesBrowser private constructor(node: JsonNode?) : ConfigBrowser(node) {
    internal constructor() : this(null)

    override val mapper = ObjectMapper(
        JavaPropsFactory()
    )

    override fun create(node: JsonNode?): ConfigBrowser {
        return PropertiesBrowser(node)
    }
}
