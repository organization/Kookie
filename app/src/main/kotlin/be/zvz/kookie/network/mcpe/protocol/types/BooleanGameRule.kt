package be.zvz.kookie.network.mcpe.protocol.types

import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

class BooleanGameRule(private val value: Boolean) : GameRule() {

    override val typeId = GameRuleType.BOOL

    fun getValue(): Boolean = value

    override fun encode(output: PacketSerializer) {
        output.putBoolean(value)
    }
}
