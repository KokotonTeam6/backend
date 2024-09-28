package kokoton.sextet.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "streak")
data class Streak(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,  // Primary Key

    @Column(nullable = false, columnDefinition = "INT")
    var days: Int? = null,

    @Column(nullable = false)
    var lastDate: LocalDateTime? = null,

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")  // 외래 키 컬럼 이름을 "user_id"로 명시
    var profile: Profile  // 1:1 관계 설정
)