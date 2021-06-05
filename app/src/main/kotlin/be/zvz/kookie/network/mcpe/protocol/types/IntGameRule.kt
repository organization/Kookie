package be.zvz.kookie.network.mcpe.protocol.types

import be.zvz.kookie.network.mcpe.protocol.serializer.PacketSerializer

class IntGameRule(private val value: Int) : GameRule() {

    override val typeId = GameRuleType.INT

    fun getValue(): Int = value

    override fun encode(output: PacketSerializer) {
        output.putUnsignedVarInt(value)
    }
}
