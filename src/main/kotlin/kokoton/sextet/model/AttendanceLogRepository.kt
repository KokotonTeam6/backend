package kokoton.sextet.model

import org.springframework.data.jpa.repository.JpaRepository

interface AttendanceLogRepository: JpaRepository<AttendanceLog, Long> {

}