package be.zvz.kookie.network.mcpe.serializer

import be.zvz.kookie.math.Vector3
import be.zvz.kookie.utils.BinaryStream
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class PacketSerializer(buffer: String = "", offset: Int = 0) : BinaryStream(buffer, offset) {

    var shieldRuntimeId: Int = 0

    fun getString(): String = get(getUnsignedVarInt())

    fun putString(v: String) {
        putUnsignedVarInt(v.length)
        put(v)
    }

    fun getUUID(): UUID {
        val p1 = get(8).reversed()
        val p2 = get(8).reversed()
        return UUID.fromString(p1 + p2)
    }

    fun putUUID(uuid: UUID) {
        /*
        $bytes = $uuid->getBytes();
		$this->put(strrev(substr($bytes, 0, 8)));
		$this->put(strrev(substr($bytes, 8, 8)));
         */
        val bytes = uuid.toString()
        put(bytes.substring(0, 8).reversed())
        put(bytes.substring(8, 8).reversed())
    }

    fun getBlockPosition(x: AtomicInteger, y: AtomicInteger, z: AtomicInteger) {
        x.set(getVarInt())
        y.set(getUnsignedVarInt())
        z.set(getVarInt())
    }

    fun putBlockPosition(x: Int, y: Int, z: Int) {
        putVarInt(x)
        putUnsignedVarInt(y)
        putVarInt(z)
    }

    // TODO: SkinData

    // TODO: SkinImage

    // TODO: ItemStack

    fun getEntityUniqueId(): Long {
        return getVarLong()
    }

    fun putEntityUniqueId(id: Long) {
        putVarLong(id)
    }

    fun getEntityRuntimeId(): Long {
        return getUnsignedVarLong()
    }

    fun putEntityRuntimeId(id: Long) {
        putUnsignedVarLong(id)
    }

    fun getVector3(): Vector3 {
        return Vector3(getLFloat(), getLFloat(), getLFloat())
    }

    fun putVector3Nullable(vector: Vector3?) {
        if (vector != null) {
            putVector3(vector)
        } else {
            putLFloat(0F)
            putLFloat(0F)
            putLFloat(0F)
        }
    }

    fun putVector3(vector: Vector3) {
        putLFloat(vector.x)
        putLFloat(vector.y)
        putLFloat(vector.z)
    }
}
