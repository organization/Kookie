package be.zvz.kookie.network.mcpe

import io.jsonwebtoken.Jwts
import java.security.KeyPair

object JwtUtils {
    fun create(header: Map<String, Any>, claims: Map<String, Any>, keyPair: KeyPair): String {
        return Jwts.builder()
            .setHeader(header)
            .setClaims(claims)
            .signWith(keyPair.private)
            .compact()
    }
}
