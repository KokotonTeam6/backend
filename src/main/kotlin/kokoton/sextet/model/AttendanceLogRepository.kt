package kokoton.sextet.model

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface AttendanceLogRepository: JpaRepository<AttendanceLog, Long> {
    @Transactional
    @Query("SELECT a FROM AttendanceLog a WHERE a.profile.id = :userId AND a.date >= :startDate")
    fun getAttendanceLogsByUserId(
        @Param("userId") userId: Long,
        @Param("startDate") startDate: LocalDateTime,
    ): List<AttendanceLog>

    @Transactional
    @Query("SELECT a FROM AttendanceLog a WHERE a.profile.id = :userId ORDER BY a.date DESC LIMIT 1")
    fun findLatestWithId(@Param("userId") userId: Long): AttendanceLog?
}