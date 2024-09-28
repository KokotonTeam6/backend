package kokoton.sextet.dto

data class AttendanceLogResposneDTO(
    val streak: Int = 0,
    val attendanceLog: List<Boolean> = listOf()
)
