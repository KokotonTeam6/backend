package kokoton.sextet.controller

import kokoton.sextet.dto.AttendanceLogResposneDTO
import kokoton.sextet.model.AttendanceLog
import kokoton.sextet.model.AttendanceLogRepository
import kokoton.sextet.model.Streak
import kokoton.sextet.model.StreakRepository
import kokoton.sextet.util.getCurrentUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@RestController
@RequestMapping("/v1/attendance")
class AttendanceController(
    @Autowired private val attendanceLogRepository: AttendanceLogRepository,
    @Autowired private val streakRepository: StreakRepository
) {
    @GetMapping("")
    fun getAttendanceLog(@RequestParam days: Long = 7): ResponseEntity<AttendanceLogResposneDTO> {
        val user = getCurrentUser()

        val streak = streakRepository.getStreakByProfileId(user.id!!)
        val attendanceLog = attendanceLogRepository.getAttendanceLogsByUserId(user.id!!, LocalDateTime.now().minusDays(days)).filter {
            it.date!! >= LocalDateTime.now().minusDays(7)
        }

        return ResponseEntity.ok(AttendanceLogResposneDTO(
            streak?.days ?: 0,
            getAttendanceForDays(attendanceLog, days.toInt())
        ))
    }

    @PutMapping("")
    fun setAttendanceLog(): ResponseEntity<AttendanceLogResposneDTO> {
        val user = getCurrentUser()

        val streak = streakRepository.getStreakByProfileId(user.id!!) ?: streakRepository.save(
            Streak(
                profile = user,
                days = 1,
                lastDate = LocalDateTime.now(),
            ))
        val date = streak.lastDate!!
        if(date <= LocalDateTime.now().minusDays(2).with(LocalTime.MIN)) {
            streak.days = 0
            streak.lastDate = LocalDateTime.now()
            streakRepository.save(streak)
        } else if(date <= LocalDateTime.now().minusDays(1).with(LocalTime.MIN)) {
            streak.days = streak.days?.plus(1)
            streak.lastDate = LocalDateTime.now()
            streakRepository.save(streak)
        }

        if(attendanceLogRepository.findLatestWithId(user.id!!) == null)
            attendanceLogRepository.save(AttendanceLog(
                date = LocalDateTime.now(),
                isAttendance = true,
                profile = user
            ))

        val attendanceLog = attendanceLogRepository.getAttendanceLogsByUserId(user.id!!, LocalDateTime.now().minusDays(7)).filter {
            it.date!! >= LocalDateTime.now().minusDays(7)
        }

        return ResponseEntity.ok(AttendanceLogResposneDTO(
            streak.days ?: 0,
            getAttendanceForDays(attendanceLog, 7)
        ))
    }

    fun getAttendanceForDays(attendanceLog: List<AttendanceLog>, days: Int): List<Boolean> {
        val attendanceForDays = MutableList(days) { false }

        // Fill in the list based on `attendanceLog`
        // Assuming `attendanceLog` is sorted by date and `logDate` is a LocalDate field
        attendanceLog.forEach { log ->
            val dayIndex = days - (LocalDate.now().toEpochDay() - log.date!!.toLocalDate().toEpochDay()).toInt() - 1

            if (dayIndex in 0 until days) {
                attendanceForDays[dayIndex] = log.isAttendance == true
            }
        }

        return attendanceForDays
    }
}