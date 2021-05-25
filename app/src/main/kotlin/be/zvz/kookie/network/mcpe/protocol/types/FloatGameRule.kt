package be.zvz.kookie.network.mcpe.protocol.types

import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

class FloatGameRule(private val value: Float) : GameRule() {

    override val typeId = GameRuleType.FLOAT

    fun getValue(): Float = value

    override fun encode(output: PacketSerializer) {
        output.putLFloat(value)
    }
}
