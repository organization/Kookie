/**
 *
 * _  __           _    _
 * | |/ /___   ___ | | _(_) ___
 * | ' // _ \ / _ \| |/ / |/ _ \
 * | . \ (_) | (_) |   <| |  __/
 * |_|\_\___/ \___/|_|\_\_|\___|
 *
 * A server software for Minecraft: Bedrock Edition
 *
 * Copyright (C) 2021 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.network.mcpe.encryption

import be.zvz.kookie.network.mcpe.JwtUtils
import org.whispersystems.curve25519.Curve25519
import java.security.KeyFactory
import java.security.KeyPair
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

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
