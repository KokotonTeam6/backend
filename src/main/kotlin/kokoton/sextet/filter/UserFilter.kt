package kokoton.sextet.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kokoton.sextet.util.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class UserFilter(
    @Autowired
    private val jwtUtil: JwtUtil,
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader(HttpHeaders.AUTHORIZATION)
        if(!token.contains("BEARER")) return filterChain.doFilter(request, response)

        val result = jwtUtil.getClaims(token.split(" ")[1].trim())

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(result, null, null)
    }
}