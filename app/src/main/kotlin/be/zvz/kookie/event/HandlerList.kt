package be.zvz.kookie.event

import be.zvz.kookie.plugin.Plugin
import com.koloboke.collect.map.hash.HashObjObjMaps

class HandlerList(val clazz: Class<out Event>, val parentHandlerList: HandlerList? = null) {
    var handlerSlots: MutableMap<EventPriority, MutableList<RegisteredListener>> =
        HashObjObjMaps.newMutableMap()
        private set

    fun register(listener: RegisteredListener) {
        if (handlerSlots.getOrPut(listener.priority) {
            mutableListOf()
        }.contains(listener)
        ) {
            throw IllegalStateException(
                "This listener is already registered to priority ${listener.priority.priority} of event ${clazz.simpleName}"
            )
        }
        handlerSlots.getOrPut(listener.priority) {
            mutableListOf()
        }.add(listener)
    }

    fun registerAll(vararg listeners: RegisteredListener) {
        listeners.forEach {
            register(it)
        }
    }

    fun unregister(obj: Plugin) {
        val iter = handlerSlots.iterator()
        while (iter.hasNext()) {
            val (priority, list) = iter.next()
            list.forEach {
                if (it.plugin == obj) {
                    iter.remove()
                }
            }
        }
    }

    fun unregister(obj: Listener) {
        val iter = handlerSlots.iterator()
        while (iter.hasNext()) {
            val (priority, list) = iter.next()
            list.forEach {
                iter.remove()
            }
        }
    }

    fun unregister(obj: RegisteredListener) {
        if (handlerSlots[obj.priority]?.contains(obj) == true) {
            handlerSlots[obj.priority]?.remove(obj)
        }
    }

    fun clear() {
        handlerSlots = HashObjObjMaps.newMutableMap()
    }

    fun getListenersByPriority(priority: EventPriority): MutableList<RegisteredListener>? = handlerSlots[priority]

    fun getParent(): HandlerList? = parentHandlerList
}
