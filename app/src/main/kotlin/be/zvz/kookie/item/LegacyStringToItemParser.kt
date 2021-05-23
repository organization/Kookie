package be.zvz.kookie.item

import com.koloboke.collect.map.hash.HashObjIntMaps

object LegacyStringToItemParser {
    init {
        /*
        $mappingsRaw = @file_get_contents(\pocketmine\RESOURCE_PATH . '/item_from_string_bc_map.json');
        if($mappingsRaw === false) throw new AssumptionFailedError("Missing required resource file");

        $mappings = json_decode($mappingsRaw, true);
        if(!is_array($mappings)) throw new AssumptionFailedError("Invalid mappings format, expected array");

        foreach($mappings as $name => $id){
            if(!is_string($name) or !is_int($id)) throw new AssumptionFailedError("Invalid mappings format, expected string keys and int values");
            $result->addMapping($name, $id);
        }
         */
    }

    private val map: MutableMap<String, Int> = HashObjIntMaps.newMutableMap()

    fun addMapping(alias: String, id: Int) {
        map[alias] = id
    }

    fun parseId(input: String): Int? = map[reprocess(input).lowercase()]

    fun parse(input: String): Item {
        val key = reprocess(input)
        val b = key.split(":")

        val meta: Int? = if (b.size < 0) {
            0
        } else {
            b[1].toIntOrNull()
        }

        if (meta == null) {
            throw IllegalArgumentException("Unable to parse \"${b[1]}\" from \"$input\" as a valid meta value")
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
