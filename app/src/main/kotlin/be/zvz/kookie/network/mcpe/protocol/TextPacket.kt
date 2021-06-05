package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

@ProtocolIdentify(ProtocolInfo.IDS.TEXT_PACKET)
class TextPacket : DataPacket(), ClientboundPacket, ServerboundPacket {

    var type: Int = -1
    var needTranslation: Boolean = false
    var sourceName: String = ""
    var message: String = ""
    var parameters: MutableList<String> = mutableListOf()
    var xboxUserId: String = ""
    var platformChatId: String = ""

    override fun decodePayload(input: PacketSerializer) {
        type = input.getByte()
        needTranslation = input.getBoolean()
        if (type in TYPE_CHAT..TYPE_ANNOUNCEMENT) {
            sourceName = input.getString()
        }
        if (type in TYPE_CHAT..TYPE_JSON) {
            message = input.getString()
        }
        if (type in TYPE_TRANSLATION..TYPE_JUKEBOX_POPUP) {
            message = input.getString()
            for (i in 0 until input.getUnsignedVarInt()) {
                parameters.add(input.getString())
            }
        }
        xboxUserId = input.getString()
        platformChatId = input.getString()
    }

    override fun encodePayload(output: PacketSerializer) {
        output.putByte(type)
        output.putBoolean(needTranslation)
        if (type in TYPE_CHAT..TYPE_ANNOUNCEMENT) {
            output.putString(sourceName)
        }
        if (type in TYPE_CHAT..TYPE_JSON) {
            output.putString(message)
        }
        if (type in TYPE_TRANSLATION..TYPE_JUKEBOX_POPUP) {
            output.putString(message)
            output.putUnsignedVarInt(parameters.size)
            parameters.forEach {
                output.putString(it)
            }
        }
        output.putString(xboxUserId)
        output.putString(platformChatId)
    }

    override fun handle(handler: PacketHandlerInterface): Boolean {
        return handler.handleText(this)
    }

    companion object {
        const val TYPE_RAW = 0
        const val TYPE_CHAT = 1
        const val TYPE_TRANSLATION = 2
        const val TYPE_POPUP = 3
        const val TYPE_JUKEBOX_POPUP = 4
        const val TYPE_TIP = 5
        const val TYPE_SYSTEM = 6
        const val TYPE_WHISPER = 7
        const val TYPE_ANNOUNCEMENT = 8
        const val TYPE_JSON_WHISPER = 9
        const val TYPE_JSON = 10

        private fun messageOnly(type: Int, message: String): TextPacket {
            val result = TextPacket()
            result.type = type
            result.message = message
            return result
        }

        fun raw(message: String): TextPacket {
            return messageOnly(TYPE_CHAT, message)
        }

        private fun baseTranslation(type: Int, key: String, parameters: MutableList<String>): TextPacket {
            val result = messageOnly(type, key)
            result.needTranslation = true
            result.parameters = parameters
            return result
        }

        fun translation(key: String, parameters: MutableList<String>): TextPacket {
            return baseTranslation(TYPE_TRANSLATION, key, parameters)
        }

        fun popup(message: String): TextPacket {
            return messageOnly(TYPE_POPUP, message)
        }

        fun translatedPopup(key: String, parameters: MutableList<String>): TextPacket {
            return baseTranslation(TYPE_POPUP, key, parameters)
        }

        fun jukeboxPopup(key: String, parameters: MutableList<String>): TextPacket {
            return baseTranslation(TYPE_JUKEBOX_POPUP, key, parameters)
        }

        fun tip(message: String): TextPacket {
            return messageOnly(TYPE_TIP, message)
        }
    }
}
