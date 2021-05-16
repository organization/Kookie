package be.zvz.kookie.utils

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

class JsonBrowser private constructor(node: JsonNode?) : ConfigBrowser(node) {
    internal constructor() : this(null)

    override val mapper = ObjectMapper(
        JsonFactory().apply {
            enable(JsonParser.Feature.ALLOW_COMMENTS)
            enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
        }
    )

    override fun create(node: JsonNode?): ConfigBrowser {
        return JsonBrowser(node)
    }
}
