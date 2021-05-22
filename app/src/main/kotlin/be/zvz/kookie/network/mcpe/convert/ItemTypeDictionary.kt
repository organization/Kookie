package be.zvz.kookie.network.mcpe.convert

import be.zvz.kookie.network.mcpe.protocol.ItemTypeEntry
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.afterburner.AfterburnerModule
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import java.lang.NumberFormatException

class ItemTypeDictionary private constructor(val itemTypes: List<ItemTypeEntry>) {
    private val stringToIntMap: MutableMap<String, Int> = mutableMapOf()
    private val intToStringIdMap: MutableMap<Int, String> = mutableMapOf()
    companion object {
        private val jsonMapper: ObjectMapper = jacksonMapperBuilder()
            .addModule(AfterburnerModule())
            .build()
        private lateinit var instance: ItemTypeDictionary
        fun getInstance(): ItemTypeDictionary {
            return instance
        }

        fun make() {
            val data = jsonMapper.readValue(
                this::class.java.getResourceAsStream("vanilla/required_item_list.json"),
                object : TypeReference<Map<String, Map<String, String>>>() {}
            )

            val itemTypes: MutableList<ItemTypeEntry> = mutableListOf()
            data.forEach { (name, entry) ->
                if (!entry.containsKey("component_based") || !entry.containsKey("runtime_id")) {
                    // error
                    return
                }

                val runtimeId: Int
                val componentBased: Boolean
                try {
                    runtimeId = entry.getValue("runtime_id").toInt()
                    componentBased = entry.getValue("component_based").toBoolean()
                } catch (ignored: NumberFormatException) {
                    return
                }

                itemTypes.add(ItemTypeEntry(name, runtimeId, componentBased))
            }
            instance = ItemTypeDictionary(itemTypes)
        }
    }
    init {
        itemTypes.forEach { type ->
            stringToIntMap[type.stringId] = type.numericId
            intToStringIdMap[type.numericId] = type.stringId
        }
    }
    fun getEntires(): List<ItemTypeEntry> = itemTypes
    fun fromStringId(id: String): Int {
        return stringToIntMap.getValue(id)
    }
    fun fromIntId(id: Int): String {
        return intToStringIdMap.getValue(id)
    }
}
