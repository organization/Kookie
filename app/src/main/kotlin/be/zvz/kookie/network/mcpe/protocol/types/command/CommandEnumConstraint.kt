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
 * Copyright (C) 2021 - 2022 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.network.mcpe.protocol.types.command

class CommandEnumConstraint(
    private val enum: CommandEnum,
    private val valueOffset: Int,
    private val constraints: MutableList<Int>
) {

    fun getEnum(): CommandEnum = enum

    fun getValueOffset(): Int = valueOffset

    fun getConstraints(): MutableList<Int> = constraints

    fun getAffectedValue(): String {
        return enum.getEnumValues()[valueOffset]
    }
}
