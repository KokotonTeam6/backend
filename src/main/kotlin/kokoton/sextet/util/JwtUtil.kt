package kokoton.sextet.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import kokoton.sextet.model.Profile
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.Date
import java.util.UUID

@Component
class JwtUtil(
    @Value("\${jwt.secret}") private var key: String,
    @Value("\${jwt.expiration_time}") private var exp: Long = 0
) {
    private val parser = Jwts.parser().setSigningKey(key).build()

    fun generateToken(user: Profile, extraClaims: MutableMap<String, String>): String {
        val now = Date()

        extraClaims["nickname"] = user.nickname.toString()
        extraClaims["id"] = user.id.toString()
        extraClaims["username"] = user.username.toString()

        return Jwts.builder()
            .setSubject(user.username)
            .setHeader(createHeader())
            .setClaims(extraClaims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + exp * 1000))
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

    fun getClaims(token: String): Jws<Claims?>? {
        return parser.parseClaimsJws(token)
    }

    fun isTokenValid(body: Jws<Claims?>): Boolean {
        val expires = body.body?.expiration

        return expires?.after(Date()) == true
    }
}

fun getCurrentUser(): Profile {
    return SecurityContextHolder.getContext().authentication.principal as Profile
}