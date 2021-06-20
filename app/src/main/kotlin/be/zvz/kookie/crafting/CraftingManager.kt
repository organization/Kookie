package be.zvz.kookie.crafting

import be.zvz.kookie.item.Item
import com.koloboke.collect.map.hash.HashObjObjMaps

class CraftingManager {

    val shapedRecipes: MutableMap<String, MutableList<ShapedRecipe>> = HashObjObjMaps.newMutableMap()

    val shapelessRecipes: MutableMap<String, MutableList<ShapelessRecipe>> = HashObjObjMaps.newMutableMap()

    val furnaceRecipeManager: FurnaceRecipeManager = FurnaceRecipeManager()

    val recipeRegisteredCallback: MutableList<() -> Unit> = mutableListOf()

    init {
        furnaceRecipeManager.recipeRegisteredCallbacks.add {
            recipeRegisteredCallback.forEach {
                it()
            }
        }
    }

    companion object {
        @JvmStatic
        fun sort(i1: Item, i2: Item): Int {
            var retval = i1.getId().compareTo(i2.getId())
            if (retval == 0) {
                retval = i1.getMeta().compareTo(i2.getMeta())
                if (retval == 0) {
                    retval = i1.count.compareTo(i2.count)
                }
            }
            return retval
        }

        @JvmStatic
        private fun pack(items: MutableList<Item>): MutableList<Item> {
            val result = mutableListOf<Item>()
            items.forEachIndexed index@{ index, item ->
                result.forEach {
                    if (item.equals(it)) {
                        it.count = it.count + item.count
                        return@index
                    }
                }
                result.add(item.clone())
            }
            return result
        }

        @JvmStatic
        private fun hashOutputs(outputs: MutableList<Item>): String {
            /*
            TODO:
            val outputs = pack(outputs)
            outputs.sortWith(
                Comparator { item, item2 ->
                    sort(item, item2)
                }
            )
            outputs.forEach { o ->
                o.count = 1
            }

             */
            return ""
        }
    }
}
