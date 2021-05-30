package be.zvz.kookie.network.mcpe.protocol.serializer

import be.zvz.kookie.network.mcpe.protocol.types.ItemTypeEntry
import com.koloboke.collect.map.hash.HashIntObjMaps
import com.koloboke.collect.map.hash.HashObjObjMaps

class ItemTypeDictionary(val itemTypes: List<ItemTypeEntry>) {

    private val stringToIntMap: MutableMap<String, Int> = HashObjObjMaps.newMutableMap()
    private val intToStringIdMap: MutableMap<Int, String> = HashIntObjMaps.newMutableMap()

    init {
        itemTypes.forEach { type ->
            stringToIntMap[type.stringId] = type.numericId
            intToStringIdMap[type.numericId] = type.stringId
        }
    }

    fun getEntires(): List<ItemTypeEntry> = itemTypes

    fun fromStringId(id: String): Int = stringToIntMap.getValue(id)

    fun fromIntId(id: Int): String = intToStringIdMap.getValue(id)
}
