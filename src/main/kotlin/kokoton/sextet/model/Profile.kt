package kokoton.sextet.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "profile")
data class Profile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable=false, columnDefinition = "VARCHAR(26)")
    var username: String? = null,

    @Column(nullable=false, columnDefinition = "VARCHAR(255)")
    var password: String? = null,

    @Column(nullable=false, columnDefinition = "VARCHAR(20)")
    var nickname: String? = null,

    @Column(nullable=false, columnDefinition = "VARCHAR(2048)")
    var profileUrl: String? = "",

    @Column(nullable=false)
    var xp: Int = 0,

    @Column(nullable=false,)
    var point: Int = 0,

    @Column(nullable=false)
    var createdAt: LocalDateTime? = LocalDateTime.now(),

    @Column(nullable=false)
    var updatedAt: LocalDateTime? = LocalDateTime.now(),
)