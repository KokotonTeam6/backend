package kokoton.sextet.model

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ProfileRepository: JpaRepository<Profile, Long> {
    @Transactional
    fun findById(username: String): Profile?
}