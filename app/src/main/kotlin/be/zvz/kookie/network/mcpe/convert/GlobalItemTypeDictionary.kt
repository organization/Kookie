package be.zvz.kookie.network.mcpe.convert

import be.zvz.kookie.network.mcpe.protocol.serializer.ItemTypeDictionary
import be.zvz.kookie.network.mcpe.protocol.types.ItemTypeEntry
import be.zvz.kookie.utils.Json
import com.fasterxml.jackson.core.type.TypeReference
import java.lang.NumberFormatException

class GlobalItemTypeDictionary private constructor(val dictionary: ItemTypeDictionary) {

    companion object {
        private val instance: GlobalItemTypeDictionary = make()
        fun getInstance(): GlobalItemTypeDictionary {
            return instance
        }

        private fun make(): GlobalItemTypeDictionary {
            val data = Json.jsonMapper.readValue(
                this::class.java.getResourceAsStream("vanilla/required_item_list.json"),
                object : TypeReference<Map<String, Map<String, String>>>() {}
            )

            val itemTypes: MutableList<ItemTypeEntry> = mutableListOf()
            data.forEach { (name, entry) ->
                val runtimeId: Int
                val componentBased: Boolean
                try {
                    runtimeId = entry.getValue("runtime_id").toInt()
                    componentBased = entry.getValue("component_based").toBoolean()
                } catch (ignored: NumberFormatException) {
                    return@forEach
                } catch (ignored: NoSuchElementException) {
                    return@forEach
                }

                itemTypes.add(ItemTypeEntry(name, runtimeId, componentBased))
            }
            return GlobalItemTypeDictionary(ItemTypeDictionary(itemTypes))
        }
    }
}