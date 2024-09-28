package kokoton.sextet.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import kokoton.sextet.model.Profile
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import java.util.UUID

@Component
class JwtUtil(
    @Value("\${jwt.secret}") private var key: String,
    @Value("\${jwt.expiration_time}") private var exp: Long = 0
) {
    private val parser = Jwts.parser().setSigningKey(key).build()

    fun generateToken(user: Profile): String {
        val now = Date()

        return Jwts.builder()
            .setSubject(user.id!!)
            .setHeader(createHeader())
            .setClaims(createClaims(user))
            .setIssuedAt(now)
            .setExpiration(Date(now.time + exp))
            .signWith(SignatureAlgorithm.HS256, key)
            .compact()
    }

    private fun createHeader(): Map<String, Any> {
        val header = HashMap<String, Any>()

        header["alg"] = "HS256"
        header["typ"] = "JWT"
        header["nonce"] = UUID.randomUUID().toString()
        header["regDate"] = Date(System.currentTimeMillis())

        return header
    }

    private fun createClaims(user: Profile): Map<String, Any> {
        val claims = HashMap<String, Any>()
        claims["sub"] = user.id!!
        claims["username"] = user.nickname!!

        return claims
    }

    fun getClaims(token: String): Map<String, Any> {
        return parser.parseClaimsJwt(token).body
    }

    fun getUsername(value: Map<String, Any>): String {
        return value["username"] as String
    }
}