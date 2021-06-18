package be.zvz.kookie.event

import be.zvz.kookie.plugin.Plugin
import com.koloboke.collect.map.hash.HashObjObjMaps
import java.lang.reflect.Modifier

object HandlerListManager {
    var handlers: MutableMap<Class<out Event>, HandlerList> = HashObjObjMaps.newMutableMap()

    @JvmStatic
    fun unregisterAll(obj: Any? = null) {
        when (obj) {
            is Plugin -> {
                handlers.forEach { (_, it) ->
                    it.unregister(obj)
                }
            }
            else -> {
                handlers.forEach { (_, it) ->
                    it.clear()
                }
            }
        }
    }

    fun getListFor(event: Class<out Event>): HandlerList {
        if (handlers.contains(event)) {
            return handlers.getValue(event)
        }
        if (!isValid(event)) {
            throw IllegalArgumentException(
                "Event must be non-abstract or have the ${AllowAbstract::class.java.simpleName} annotation"
            )
        }
        val parent = resolveNearestHandleableParent(event)
        return handlers.put(
            event,
            HandlerList(event, if (parent != null) getListFor(parent as Class<out Event>) else null)
        )!! // TODO: unchecked cast
    }

    private fun isValid(clazz: Class<*>): Boolean =
        Modifier.isAbstract(clazz.modifiers) || clazz.getAnnotation(AllowAbstract::class.java) != null

    private fun resolveNearestHandleableParent(clazz: Class<out Event>): Class<*>? {
        var parent = clazz.superclass
        while (parent !== null) {
            if (isValid(parent)) {
                return parent
            }
            parent = parent.superclass
        }
        return null
    }
}
