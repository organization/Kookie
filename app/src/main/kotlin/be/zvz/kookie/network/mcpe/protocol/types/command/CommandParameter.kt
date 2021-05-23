package be.zvz.kookie.network.mcpe.protocol.types.command

import be.zvz.kookie.network.mcpe.protocol.AvailableCommandsPacket

class CommandParameter(
    var paramName: String = "",
    var paramType: Int = -1,
    var isOptional: Boolean = false,
    var flags: Int = -1,
    var enum: CommandEnum? = null,
    var postfix: String? = null
) {

    companion object {
        const val FLAG_FORCE_COLLAPSE_ENUM = 0x1
        const val FLAG_HAS_ENUM_CONSTRAINT = 0x2

        private fun baseline(name: String, type: Int, flags: Int, optional: Boolean): CommandParameter {
            return CommandParameter(name, type, optional, flags, null, null)
        }

        fun standard(name: String, type: Int, flags: Int, optional: Boolean = false): CommandParameter {
            return baseline(name, AvailableCommandsPacket.ARG_FLAG_VALID or type, flags, optional)
        }

        fun postfixed(name: String, postfix: String, flags: Int, optional: Boolean = false): CommandParameter {
            val result = baseline(name, AvailableCommandsPacket.ARG_FLAG_POSTFIX, flags, optional)
            result.postfix = postfix
            return result
        }
    }
}