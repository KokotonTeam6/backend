package kokoton.sextet.service

import kokoton.sextet.model.ProfileRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    @Autowired val userRepository: ProfileRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)

        if(user == null) {
            throw UsernameNotFoundException("User not found!")
        }

        return User(user.username, user.password, listOf(SimpleGrantedAuthority("USER")))
    }
}