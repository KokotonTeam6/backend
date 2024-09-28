package kokoton.sextet.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "attendance_log")
data class AttendanceLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,  // Primary Key

    @Column(nullable = false, columnDefinition = "BOOLEAN")
    var isAttendance: Boolean? = null,

    @Column(nullable = false)
    var date: LocalDateTime? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")  // 외래 키를 "user_id"로 명시
    var profile: Profile? = null  // 1:N 관계 설정
)