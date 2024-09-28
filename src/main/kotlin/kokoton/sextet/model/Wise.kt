package kokoton.sextet.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "wise")
data class Wise(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String? = null,

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    var author: String? = null,

    @Column(nullable = true)
    var lastShown: LocalDateTime? = null,
)
