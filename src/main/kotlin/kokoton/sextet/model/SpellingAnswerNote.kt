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
@Table(name = "spelling_answer_note")
data class SpellingAnswerNote(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    // Many-to-One relationship with Profile
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false) // Foreign key to Profile table
    var user: Profile? = null,

    // Many-to-One relationship with SpellingQuiz
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quiz_id", nullable = false) // Foreign key to SpellingQuiz table
    var quiz: SpellingQuiz? = null,

    // Answer for the spelling quiz
    @Column(nullable = false, columnDefinition = "SMALLINT")
    var answer: Int? = 0, // Assuming the range of the answer is suitable for SmallInt (Short in Kotlin)

    // Date and time the answer was created
    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    // Date and time the answer was created
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()

)