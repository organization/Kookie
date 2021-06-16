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
package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer
import be.zvz.kookie.network.mcpe.protocol.types.command.CommandData
import be.zvz.kookie.network.mcpe.protocol.types.command.CommandEnum
import be.zvz.kookie.network.mcpe.protocol.types.command.CommandEnumConstraint
import be.zvz.kookie.network.mcpe.protocol.types.command.CommandParameter
import com.koloboke.collect.map.hash.HashIntObjMaps
import com.koloboke.collect.map.hash.HashObjIntMaps
import com.koloboke.collect.map.hash.HashObjObjMaps

@ProtocolIdentify(ProtocolInfo.IDS.AVAILABLE_COMMANDS_PACKET)
class AvailableCommandsPacket : DataPacket(), ClientboundPacket {

    val commandData: MutableMap<String, CommandData> = HashObjObjMaps.newMutableMap()

    val hardCodeEnums = mutableListOf<CommandEnum>()

    val softEnums = mutableListOf<CommandEnum>()

    val enumConstraints = mutableListOf<CommandEnumConstraint>()

    override fun decodePayload(input: PacketSerializer) {
        val enumValues = mutableListOf<String>().apply {
            repeat(input.getUnsignedVarInt() - 1) {
                add(input.getString())
            }
        }
        val postfixes = mutableListOf<String>().apply {
            repeat(input.getUnsignedVarInt() - 1) {
                add(input.getString())
            }
        }
        val enums = mutableListOf<CommandEnum>()
        repeat(input.getUnsignedVarInt() - 1) {
            val enum = getEnum(enumValues, input)
            enums.add(enum)
            if (HARDCODED_ENUM_NAMES.containsKey(enum.getEnumName())) {
                hardCodeEnums.add(enum)
            }
        }
        repeat(input.getUnsignedVarInt() - 1) {
            val command = getCommandData(enums, postfixes, input)
            commandData[command.name] = command
        }
        repeat(input.getUnsignedVarInt() - 1) {
            softEnums.add(getSoftEnum(input))
        }
        repeat(input.getUnsignedVarInt() - 1) {
            enumConstraints.add(getEnumConstraint(enums, enumValues, input))
        }
    }

    override fun encodePayload(output: PacketSerializer) {
        val enumIndexes: MutableMap<String, Int> = HashObjIntMaps.newMutableMap()
        val enumValueIndexes: MutableMap<String, Int> = HashObjIntMaps.newMutableMap()
        val postfixIndexes: MutableMap<String, Int> = HashObjIntMaps.newMutableMap()
        val enums: MutableMap<Int, CommandEnum> = HashIntObjMaps.newMutableMap()

        val addEnum = fun(enum: CommandEnum) {
            if (!enumIndexes.containsKey(enum.getEnumName())) {
                val size = enumIndexes.size
                enumIndexes[enum.getEnumName()] = size
                enums[size] = enum
            }
            enum.getEnumValues().forEach {
                enumValueIndexes[it] = enumValueIndexes.getOrDefault(it, enumValueIndexes.size)
            }
        }

        val addPostfix = fun(postfix: String) {
            postfixIndexes[postfix] = postfixIndexes.getOrDefault(postfix, postfixIndexes.size)
        }

        hardCodeEnums.forEach(addEnum)

        commandData.forEach { (_, data) ->
            data.aliases?.let(addEnum)

            data.overloads.forEach { (_, map) ->
                map.forEach { (_, commandParameter) ->
                    commandParameter.enum?.let(addEnum)

                    commandParameter.postfix?.let(addPostfix)
                }
            }
        }
        output.putUnsignedVarInt(enumValueIndexes.size)
        enumValueIndexes.forEach { (enumValue, _) ->
            output.putString(enumValue)
        }

        output.putUnsignedVarInt(postfixIndexes.size)
        postfixIndexes.forEach { (postfix, _) ->
            output.putString(postfix)
        }

        output.putUnsignedVarInt(enums.size)
        enums.forEach { (_, commandEnum) ->
            putEnum(commandEnum, enumValueIndexes, output)
        }

        output.putUnsignedVarInt(commandData.size)
        commandData.forEach { (_, commandData) ->
            putCommandData(commandData, enumIndexes, postfixIndexes, output)
        }

        output.putUnsignedVarInt(softEnums.size)
        softEnums.forEach {
            putSoftEnum(it, output)
        }

        output.putUnsignedVarInt(enumConstraints.size)
        enumConstraints.forEach {
            putEnumConstraint(it, enumIndexes, enumValueIndexes, output)
        }
    }

    override fun handle(handler: PacketHandlerInterface): Boolean = false

    private fun getEnum(enumList: MutableList<String>, input: PacketSerializer): CommandEnum {
        val enumName = input.getString()
        val enumValues = mutableListOf<String>()

        val listSize = enumList.size
        repeat(input.getUnsignedVarInt() - 1) {
            val index = getEnumValueIndex(listSize, input)
            val enumValue = enumList.getOrNull(index) ?: throw PacketDecodeException("Invalid enum value index $index")
            enumValues.add(enumValue)
        }
        return CommandEnum(enumName, enumValues)
    }

    private fun putEnum(enum: CommandEnum, enumValueMap: MutableMap<String, Int>, output: PacketSerializer) {
        output.putString(enum.getEnumName())

        val values = enum.getEnumValues()
        output.putUnsignedVarInt(values.size)

        values.forEach {
            val index = enumValueMap.getOrDefault(it, -1)
            if (index == -1) {
                throw AssertionError("Enum value '$it' not found")
            }
            putEnumValueIndex(index, values.size, output)
        }
    }

    private fun getEnumValueIndex(valueCount: Int, input: PacketSerializer): Int = when {
        valueCount < 256 -> {
            input.getByte()
        }
        valueCount < 65536 -> {
            input.getLShort()
        }
        else -> {
            input.getLInt()
        }
    }

    private fun putEnumValueIndex(index: Int, valueCount: Int, output: PacketSerializer) {
        when {
            valueCount < 256 -> {
                output.putByte(index)
            }
            valueCount < 65536 -> {
                output.putLShort(index)
            }
            else -> {
                output.putLInt(index)
            }
        }
    }

    private fun getCommandData(
        enums: MutableList<CommandEnum>,
        postFixes: MutableList<String>,
        input: PacketSerializer
    ): CommandData {
        val name = input.getString()
        val description = input.getString()
        val flags = input.getByte()
        val permission = input.getByte()
        val aliases = enums.getOrNull(input.getLInt())
        val overloads: MutableMap<Int, MutableMap<Int, CommandParameter>> = HashIntObjMaps.newMutableMap()

        val overloadCount = input.getUnsignedVarInt()

        repeat(overloadCount) { overloadIndex ->
            if (!overloads.containsKey(overloadIndex)) {
                overloads[overloadIndex] = HashIntObjMaps.newMutableMap()
            }
            val paramCount = input.getUnsignedVarInt()
            repeat(paramCount) { paramIndex ->
                val parameter = CommandParameter()
                parameter.paramName = input.getString()
                parameter.paramType = input.getLInt()
                parameter.isOptional = input.getBoolean()
                parameter.flags = input.getByte()

                when {
                    (parameter.paramType and ARG_FLAG_ENUM) != 0 -> {
                        val index = (parameter.paramType and 0xffff)
                        enums.getOrNull(index)?.let {
                            parameter.enum = it
                        } ?: throw PacketDecodeException(
                            "Deserializing $name parameter ${parameter.paramName}: " +
                                "Expected enum at $index, but got none"
                        )
                    }
                    (parameter.paramType and ARG_FLAG_POSTFIX) != 0 -> {
                        val index = (parameter.paramType and 0xffff)
                        postFixes.getOrNull(index)?.let {
                            parameter.postfix = it
                        } ?: throw PacketDecodeException(
                            "Deserializing $name parameter ${parameter.paramName}: " +
                                "Expected postfix at $index, but got none"
                        )
                    }
                    (parameter.paramType and ARG_FLAG_VALID) == 0 -> {
                        throw PacketDecodeException(
                            "Deserializing $name parameter ${parameter.paramName}: " +
                                "Invalid parameter type ${parameter.paramType}"
                        )
                    }
                }
                overloads.getValue(overloadIndex)[paramIndex] = parameter
            }
        }
        return CommandData(name, description, flags, permission, aliases, overloads)
    }

    private fun putCommandData(
        data: CommandData,
        enumIndexes: Map<String, Int>,
        postFixIndexes: Map<String, Int>,
        output: PacketSerializer
    ) {
        output.putString(data.name)
        output.putString(data.description)
        output.putByte(data.flags)
        output.putByte(data.permission)

        data.aliases?.let {
            output.putLInt(enumIndexes.getOrDefault(it.getEnumName(), -1))
        } ?: output.putLInt(-1)

        output.putUnsignedVarInt(data.overloads.size)
        data.overloads.forEach { (_, overload) ->
            output.putUnsignedVarInt(overload.size)
            overload.forEach { (_, parameter) ->
                output.putString(parameter.paramName)

                val type = parameter.enum?.let {
                    ARG_FLAG_ENUM or ARG_FLAG_VALID or enumIndexes.getOrDefault(it.getEnumName(), -1)
                } ?: parameter.postfix?.let {
                    val key = postFixIndexes[it] ?: throw AssertionError("Postfix '${parameter.postfix} not in postfixes array")
                    ARG_FLAG_POSTFIX or key
                } ?: parameter.paramType

                output.putLInt(type)
                output.putBoolean(parameter.isOptional)
                output.putByte(parameter.flags)
            }
        }
    }

    private fun getSoftEnum(input: PacketSerializer) = CommandEnum(
        enumName = input.getString(),
        enumValues = mutableListOf<String>().apply {
            repeat(input.getUnsignedVarInt()) {
                add(input.getString())
            }
        }
    )

    private fun putSoftEnum(enum: CommandEnum, output: PacketSerializer) {
        output.putString(enum.getEnumName())

        val values = enum.getEnumValues()
        output.putUnsignedVarInt(values.size)
        values.forEach {
            output.putString(it)
        }
    }

    private fun getEnumConstraint(
        enums: List<CommandEnum>,
        enumValues: List<String>,
        input: PacketSerializer
    ): CommandEnumConstraint {
        val valueIndex = input.getLInt()
        val enumValue = enumValues.getOrNull(valueIndex)
            ?: throw PacketDecodeException("Enum constraint refers to unknown enum value index $valueIndex")
        val enumIndex = input.getLInt()
        val enum = enums.getOrNull(enumIndex)
            ?: throw PacketDecodeException("Enum constraint refers to unknown enum index $enumIndex")
        val valueOffset = enum.getEnumValues().indexOf(enumValue)

        if (valueOffset == -1) {
            throw PacketDecodeException(
                "Value \"${enumValues[valueIndex]}\" does not belong to enum \"${enum.getEnumName()}\""
            )
        }

        val constraintIds = mutableListOf<Int>().apply {
            repeat(input.getUnsignedVarInt()) {
                add(input.getByte())
            }
        }
        return CommandEnumConstraint(enum, valueOffset, constraintIds)
    }

    private fun putEnumConstraint(
        constraint: CommandEnumConstraint,
        enumIndexes: Map<String, Int>,
        enumValues: Map<String, Int>,
        output: PacketSerializer
    ) {
        output.putLInt(enumValues.getOrDefault(constraint.getAffectedValue(), -1))
        output.putLInt(enumIndexes.getOrDefault(constraint.getEnum().getEnumName(), -1))
        output.putUnsignedVarInt(constraint.getConstraints().size)
        constraint.getConstraints().forEach {
            output.putByte(it)
        }
    }

    companion object {
        /**
         * This flag is set on all types EXCEPT the POSTFIX type. Not completely sure what this is for, but it is required
         * for the argtype to work correctly. VALID seems as good a name as any.
         */
        const val ARG_FLAG_VALID = 0x100000

        /**
         * Basic parameter types. These must be combined with the ARG_FLAG_VALID constant.
         * ARG_FLAG_VALID | (type const)
         */
        const val ARG_TYPE_INT = 0x01
        const val ARG_TYPE_FLOAT = 0x03
        const val ARG_TYPE_VALUE = 0x04
        const val ARG_TYPE_WILDCARD_INT = 0x05
        const val ARG_TYPE_OPERATOR = 0x06
        const val ARG_TYPE_TARGET = 0x07
        const val ARG_TYPE_WILDCARD_TARGET = 0x08

        const val ARG_TYPE_FILEPATH = 0x10

        const val ARG_TYPE_STRING = 0x20

        const val ARG_TYPE_POSITION = 0x28

        const val ARG_TYPE_MESSAGE = 0x2c

        const val ARG_TYPE_RAWTEXT = 0x2e

        const val ARG_TYPE_JSON = 0x32

        const val ARG_TYPE_COMMAND = 0x3f

        /**
         * Enums are a little different: they are composed as follows:
         * ARG_FLAG_ENUM | ARG_FLAG_VALID | (enum index)
         */
        const val ARG_FLAG_ENUM = 0x200000

        /** This is used for /xp <level: int>L. It can only be applied to integer parameters. */
        const val ARG_FLAG_POSTFIX = 0x1000000

        val HARDCODED_ENUM_NAMES = mapOf(
            "CommandName" to true
        )
    }
}
