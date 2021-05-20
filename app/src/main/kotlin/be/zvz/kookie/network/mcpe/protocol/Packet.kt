package be.zvz.kookie.network.mcpe.protocol

import be.zvz.kookie.network.mcpe.handler.PacketHandlerInterface
import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

interface Packet {

    fun pid(): ProtocolInfo.IDS

    fun getName(): String

    fun canBeSentBeforeLogin(): Boolean

    fun decode(input: PacketSerializer)

    fun encode(output: PacketSerializer)

    fun handle(handler: PacketHandlerInterface)
}
