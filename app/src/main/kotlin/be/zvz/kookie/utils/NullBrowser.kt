package be.zvz.kookie.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

class NullBrowser private constructor(node: JsonNode?) : ConfigBrowser(node) {
    override val mapper: ObjectMapper
        get() = ObjectMapper()

    override fun create(node: JsonNode?): ConfigBrowser = NULL_BROWSER

    override fun put(key: String?, item: Any?) {}

    companion object {
        internal val NULL_BROWSER = NullBrowser(null)
    }
}
