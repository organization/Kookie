package be.zvz.kookie.entity

import com.koloboke.collect.map.hash.HashObjObjMaps

class AttributeMap {
    private val attributes = HashObjObjMaps.newMutableMap<String, Attribute>()

    fun add(attribute: Attribute) {
        attributes[attribute.id] = attribute
    }

    fun get(id: String): Attribute? = attributes[id]

    fun getAll(): Map<String, Attribute> = attributes

    fun needSend(): Map<String, Attribute> = attributes.filter { (_, attribute) ->
        attribute.isSyncable() && attribute.isDesynchronized()
    }
}
