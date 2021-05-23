package be.zvz.kookie.item

import be.zvz.kookie.utils.Json
import com.fasterxml.jackson.core.type.TypeReference
import java.lang.NumberFormatException

object LegacyStringToItemParser {

    private val map: MutableMap<String, Int>
    init {
        val mappingRaw = this::class.java.getResourceAsStream("item_from_string_bc_map.json")
        map = Json.jsonMapper.readValue(mappingRaw, object : TypeReference<MutableMap<String, Int>>() {})
    }

    fun addMapping(alias: String, id: Int) {
        map[alias] = id
    }

    fun parseId(input: String): Int? = map[reprocess(input).lowercase()]

    fun parse(input: String): Item {
        val key = reprocess(input)
        val b = key.split(":")

        val meta: Int = if (b.size < 0) {
            0
        } else {
            try {
                b[1].toInt()
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException("Unable to parse \"${b[1]}\" from \"$input\" as a valid meta value", e)
            }
        }

        b[0].toIntOrNull()?.let {
            return ItemFactory.get(it, meta)
        }
        map[b[0].lowercase()]?.let {
            return ItemFactory.get(it, meta)
        }

        throw IllegalArgumentException("Unable to resolve \"$input\" to a valid item")
    }

    fun reprocess(input: String): String = input.trim().replace(" ", "_").replace("minecraft:", "")
}
