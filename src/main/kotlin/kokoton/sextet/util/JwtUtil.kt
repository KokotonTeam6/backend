package kokoton.sextet.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Header
import io.jsonwebtoken.Jwt
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import kokoton.sextet.model.Profile
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
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

        return Jwts.builder()
            .setSubject(user.username)
            .setHeader(createHeader())
            .setClaims(extraClaims)
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

    fun getClaims(token: String): Jwt<Header, Claims>? {
        return parser.parseClaimsJwt(token)
    }

    fun isTokenValid(body: Jwt<Header, Claims>, user: UserDetails): Boolean {
        val username = body.body.subject
        val expires = body.body.expiration

        return (username == user.username) && expires.before(Date())
    }
}