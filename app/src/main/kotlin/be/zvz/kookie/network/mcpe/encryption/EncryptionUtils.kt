package be.zvz.kookie.network.mcpe.encryption

import be.zvz.kookie.network.mcpe.JwtUtils
import org.whispersystems.curve25519.Curve25519
import java.security.*
import java.security.spec.X509EncodedKeySpec
import java.util.*

object EncryptionUtils {
    private val cipher = Curve25519.getInstance(Curve25519.BEST)
    fun generateSharedSecret(localPrivateKey: PrivateKey, remotePublicKey: PublicKey): PrivateKey {
        val keyFactory = KeyFactory.getInstance("EC")
        return keyFactory.generatePrivate(
            X509EncodedKeySpec(cipher.calculateAgreement(remotePublicKey.encoded, localPrivateKey.encoded))
        )
    }

    private fun ByteArray.toHexString() = joinToString("") {
        Integer.toUnsignedString(java.lang.Byte.toUnsignedInt(it), 16).padStart(2, '0')
    }

    fun generateKey(secret: PrivateKey, salt: String): ByteArray {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(
            (salt + secret.encoded.toHexString().padStart(96, '0').toInt(16).toString(2))
                .toByteArray()
        )
    }

    fun generateServerHandshakeJwt(keyPair: KeyPair, salt: String): String {
        return JwtUtils.create(
            mapOf(
                "x5u" to Base64.getEncoder().encode(keyPair.private.encoded),
                "alg" to "ES384"
            ),
            mapOf(
                "salt" to salt
            ),
            keyPair
        )
    }
}
