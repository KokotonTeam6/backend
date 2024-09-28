package kokoton.sextet.model

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository

interface StreakRepository: JpaRepository<Profile, Long> {

}