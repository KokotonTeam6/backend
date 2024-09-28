package kokoton.sextet.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kokoton.sextet.model.Profile
import kokoton.sextet.model.ProfileRepository
import kokoton.sextet.util.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.Collections

@Component
class JwtAuthenticationFilter(
    @Autowired private val jwtUtil: JwtUtil, // JWT Utility to parse and validate tokens
    @Autowired private val profileRepository: ProfileRepository // UserDetailsService to load user info
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // Extract JWT token from the Authorization header
        val authHeader = request.getHeader("Authorization")
        val jwtToken: String? = if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader.substring(7) // Remove "Bearer " prefix
        } else {
            null
        }

        // Validate token and set user authentication
        if (jwtToken != null && SecurityContextHolder.getContext().authentication == null) {
            val jwtBody = jwtUtil.getClaims(jwtToken)

            if (jwtBody != null && jwtUtil.isTokenValid(jwtBody)) {
                // Extract username from token claims
                val username = jwtBody.body?.get("username", String::class.java)

                // Find user by username from the repository
                val user: Profile? = profileRepository.findByUsername(username ?: "")

                // Check if user exists
                if (user != null) {
                    // Create an authentication token
                    val authenticationToken = UsernamePasswordAuthenticationToken(
                        user, null, Collections.emptyList() // Use empty authorities list or populate as necessary
                    )

                    // Set details and authentication in the security context
                    authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authenticationToken
                }
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response)
    }
}