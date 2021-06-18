/**
 *
 * _  __           _    _
 * | |/ /___   ___ | | _(_) ___
 * | ' // _ \ / _ \| |/ / |/ _ \
 * | . \ (_) | (_) |   <| |  __/
 * |_|\_\___/ \___/|_|\_\_|\___|
 *
 * A server software for Minecraft: Bedrock Edition
 *
 * Copyright (C) 2021 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.entity

import com.koloboke.collect.map.hash.HashObjObjMaps

class AttributeMap {
    private val attributes = HashObjObjMaps.newMutableMap<Attribute.Identifier, Attribute>()

    fun add(attribute: Attribute) {
        attributes[attribute.id] = attribute
    }

    fun get(id: Attribute.Identifier): Attribute? = attributes[id]

    fun getAll(): Map<Attribute.Identifier, Attribute> = attributes

    fun needSend(): Map<Attribute.Identifier, Attribute> = attributes.filter { (_, attribute) ->
        attribute.isSyncable() && attribute.isDesynchronized()
    }
}
