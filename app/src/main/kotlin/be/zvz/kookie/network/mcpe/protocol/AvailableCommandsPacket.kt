package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.types.command.*
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer
import com.koloboke.collect.map.hash.HashIntObjMaps

@ProtocolIdentify(ProtocolInfo.IDS.AVAILABLE_COMMANDS_PACKET)
class AvailableCommandsPacket : DataPacket(), ClientboundPacket {

    val commandData = mutableListOf<CommandData>()

    val hardCodeEnums = mutableListOf<CommandEnum>()

    val softEnums = mutableListOf<CommandEnum>()

    val enumConstraints = mutableListOf<CommandEnumConstraint>()

    override fun decodePayload(input: PacketSerializer) {
        val enumValues = mutableListOf<String>().apply {
            for (i in 0..input.getUnsignedVarInt()) {
                add(input.getString())
            }
        }
        val postfixes = mutableListOf<String>().apply {
            for (i in 0..input.getUnsignedVarInt()) {
                add(input.getString())
            }
        }
        val enums = mutableListOf<CommandEnum>()
        for (i in 0..input.getUnsignedVarInt()) {
            val enum = getEnum(enumValues, input)
            enums.add(enum)
            if (HARDCODED_ENUM_NAMES.containsKey(enum.getEnumName())) {
                hardCodeEnums.add(enum)
            }
        }
        for (i in 0..input.getUnsignedVarInt()) {
            commandData.add(getCommandData(enums, postfixes, input))
        }
        for (i in 0..input.getUnsignedVarInt()) {
            softEnums.add(getSoftEnum(input))
        }
        for (i in 0..input.getUnsignedVarInt()) {
            enumConstraints.add(getEnumConstraint(enums, enumValues, input))
        }
        /*
		for($i = 0, $count = $in->getUnsignedVarInt(); $i < $count; ++$i){
			$this->enumConstraints[] = $this->getEnumConstraint($enums, $enumValues, $in);
		}
         */
    }

    override fun encodePayload(output: PacketSerializer) {
        TODO("Not yet implemented")
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return false
    }

    protected fun getEnum(enumList: MutableList<String>, input: PacketSerializer): CommandEnum {
        val enumName = input.getString()
        val enumValues = mutableListOf<String>()

        val listSize = enumList.size
        for(i in 0..input.getUnsignedVarInt()) {
            val index = getEnumValueIndex(listSize, input)
            val enumValue = enumList.getOrNull(index) ?: throw PacketDecodeException("Invalid enum value index $index")
            enumValues.add(enumValue)
        }
        return CommandEnum(enumName, enumValues)
    }

    protected fun getEnumValueIndex(valueCount: Int, input: PacketSerializer): Int = when {
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

    protected fun getCommandData(enums: MutableList<CommandEnum>, postFixes: MutableList<String>, input: PacketSerializer): CommandData {
        val name = input.getString()
        val description = input.getString()
        val flags = input.getByte()
        val permission = input.getByte()
        val aliases = enums.getOrNull(input.getLInt())
        val overloads: MutableMap<Int, MutableMap<Int, CommandParameter>> = HashIntObjMaps.newMutableMap()

        val overloadCount = input.getUnsignedVarInt()

        for (overloadIndex in 0..overloadCount) {
            if (!overloads.containsKey(overloadIndex)) {
                overloads[overloadIndex] = HashIntObjMaps.newMutableMap()
            }
            val paramCount = input.getUnsignedVarInt()
            for (paramIndex in 0..paramCount) {
                val parameter = CommandParameter()
                parameter.paramName = input.getString()
                parameter.paramType = input.getLInt()
                parameter.isOptional = input.getBoolean()
                parameter.flags = input.getByte()

                if ((parameter.paramType and ARG_FLAG_ENUM) != 0) {
                    val index = (parameter.paramType and 0xffff)
                    parameter.enum = enums.getOrNull(index)
                    if (parameter.enum !is CommandEnum) {
                        throw PacketDecodeException("Deserializing $name parameter ${parameter.paramName}: Expected enum at $index, but got none")
                    }
                } else if ((parameter.paramType and ARG_FLAG_POSTFIX) != 0) {
                    val index = (parameter.paramType and 0xffff)
                    parameter.postfix = postFixes.getOrNull(index)
                    if (parameter.postfix == null) {
                        throw PacketDecodeException("Deserializing $name parameter ${parameter.paramName}: Expected postfix at $index, but got none")
                    }
                } else if ((parameter.paramType and ARG_FLAG_VALID) == 0) {
                    throw PacketDecodeException("deserializing $name parameter $parameter->paramName: Invalid parameter type ${parameter.paramType}")
                }
                overloads.getValue(overloadIndex)[paramIndex] = parameter
            }
        }
        return CommandData(name, description, flags, permission, aliases, overloads)
    }

    protected fun getSoftEnum(input: PacketSerializer): CommandEnum {
        val enumName = input.getString()
        val enumValues = mutableListOf<String>()
        for (i in 0..input.getUnsignedVarInt()) {
            enumValues.add(input.getString())
        }
        return CommandEnum(enumName, enumValues)
    }

    /*
    protected function getEnumConstraint(array $enums, array $enumValues, PacketSerializer $in) : CommandEnumConstraint{
		//wtf, what was wrong with an offset inside the enum? :(
		$valueIndex = $in->getLInt();
		if(!isset($enumValues[$valueIndex])){
			throw new PacketDecodeException("Enum constraint refers to unknown enum value index $valueIndex");
		}
		$enumIndex = $in->getLInt();
		if(!isset($enums[$enumIndex])){
			throw new PacketDecodeException("Enum constraint refers to unknown enum index $enumIndex");
		}
		$enum = $enums[$enumIndex];
		$valueOffset = array_search($enumValues[$valueIndex], $enum->getValues(), true);
		if($valueOffset === false){
			throw new PacketDecodeException("Value \"" . $enumValues[$valueIndex] . "\" does not belong to enum \"" . $enum->getName() . "\"");
		}

		$constraintIds = [];
		for($i = 0, $count = $in->getUnsignedVarInt(); $i < $count; ++$i){
			$constraintIds[] = $in->getByte();
		}

		return new CommandEnumConstraint($enum, $valueOffset, $constraintIds);
	}
     */

    protected fun getEnumConstraint(enums: MutableList<CommandEnum>, enumValues: MutableList<String>, input: PacketSerializer): CommandEnumConstraint {
        val valueIndex = input.getLInt()
        val enumValue = enumValues.getOrNull(valueIndex)
            ?: throw PacketDecodeException("Enum constraint refers to unknown enum value index $valueIndex")
        val enumIndex = input.getLInt()
        val enum = enums.getOrNull(enumIndex) ?: throw PacketDecodeException("Enum constraint refers to unknown enum index $enumIndex")
        val valueOffset = let {
            enum.getEnumValues().forEach {
                val index = enumValues.indexOf(it)
                if (index != -1) {
                    return@let index
                }
            }
            return@let -1
        }

        if (valueOffset == -1) {
            throw PacketDecodeException("Value \"${enumValues[valueIndex]}\" does not belong to enum \"${enum.getEnumName()}\"")
        }

        val constraintIds = mutableListOf<Int>().apply {
            for (i in 0..input.getUnsignedVarInt()) {
                add(input.getByte())
            }
        }
        return CommandEnumConstraint(enum, valueOffset, constraintIds)
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