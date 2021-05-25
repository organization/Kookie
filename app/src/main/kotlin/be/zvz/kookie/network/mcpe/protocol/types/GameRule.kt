package be.zvz.kookie.network.mcpe.protocol.types

import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

abstract class GameRule {
    abstract val typeId: Int

    abstract fun encode(output: PacketSerializer)
}