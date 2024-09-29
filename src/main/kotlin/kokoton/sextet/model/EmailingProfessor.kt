package kokoton.sextet.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "emailing_professor")
data class EmailingProfessor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    // Many-to-One relationship with Profile
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false) // Foreign key to Profile table
    var user: Profile? = null,

    // Title of the email
    @Column(nullable = false, length = 255)
    var emailTitle: String = "",

    // Content of the email
    @Column(nullable = false, columnDefinition = "TEXT")
    var emailContent: String = "",

    @Column(nullable = false, columnDefinition = "TEXT")
    var output: String = "",

    // Score associated with the email
    @Column(nullable = false)
    var score: Short = 0, // Assuming the range of the score is suitable for SmallInt (Short in Kotlin)

    // Date and time the email was created
    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
)
