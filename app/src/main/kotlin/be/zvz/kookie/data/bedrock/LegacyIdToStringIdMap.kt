package be.zvz.kookie.data.bedrock

import be.zvz.kookie.utils.config.JsonBrowser
import com.koloboke.collect.map.hash.HashIntObjMaps
import com.koloboke.collect.map.hash.HashObjIntMaps

enum class LegacyIdToStringIdMap(path: String) {
    ENTITY("entity_id_map"),
    ITEM("item_id_map"),
    BLOCK("block_id_map");

    val legacyToString: MutableMap<Int, String> = HashIntObjMaps.newMutableMap()
    val stringToLegacy: MutableMap<String, Int> = HashObjIntMaps.newMutableMap()

    init {
        val data = JsonBrowser().parse(this::class.java.getResourceAsStream("/vanilla/$path.json")).toMap<String, Int>()
        data.forEach { (stringId, legacyId) ->
            legacyToString[legacyId] = stringId
            stringToLegacy[stringId] = legacyId
        }
    }
}