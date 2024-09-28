package kokoton.sextet.filter

import kokoton.sextet.model.Profile
import kokoton.sextet.model.ProfileRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailService(
    @Autowired
    private val profileRepository: ProfileRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails? {
        val user = profileRepository.findById(username)

        if(user == null) {
            throw UsernameNotFoundException(username)
        }

        return
    }
}