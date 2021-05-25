package be.zvz.kookie.network.mcpe.protocol.types

import be.zvz.kookie.network.mcpe.serializer.PacketSerializer

class PlayerMovementSettings(
    private val movementType: Int,
    private val rewindSize: Int,
    private val serverAuthoritativeBlockBreaking: Boolean
) {

    fun getMovementType(): Int = movementType

    fun getRewindSize(): Int = rewindSize

    fun isServerAuthoritativeBlockBreaking(): Boolean = serverAuthoritativeBlockBreaking

    fun write(output: PacketSerializer) {
        /*
        $out->putVarInt($this->movementType);
		$out->putVarInt($this->rewindHistorySize);
		$out->putBool($this->serverAuthoritativeBlockBreaking);
         */
        output.putVarInt(movementType)
        output.putVarInt(rewindSize)
        output.putBoolean(serverAuthoritativeBlockBreaking)
    }

    companion object {
        fun read(input: PacketSerializer): PlayerMovementSettings {
            return PlayerMovementSettings(
                input.getVarInt(),
                input.getVarInt(),
                input.getBoolean()
            )
        }
    }
}
