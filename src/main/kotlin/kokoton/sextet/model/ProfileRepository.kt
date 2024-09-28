package kokoton.sextet.model

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository

interface ProfileRepository: JpaRepository<Profile, Long> {
    @Transactional
    fun findByUsername(username: String): Profile?
}