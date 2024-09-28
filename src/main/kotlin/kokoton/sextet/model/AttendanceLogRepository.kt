package kokoton.sextet.model

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository

interface AttendanceLogRepository: JpaRepository<Profile, Long> {

}