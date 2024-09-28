package kokoton.sextet.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "spelling_quiz")
data class SpellingQuiz(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable=false, columnDefinition = "VARCHAR(255)")
    var question: String? = null,

    @Column(nullable=false, columnDefinition = "VARCHAR(255)")
    var option: String? = null,

    @Column(nullable=false, columnDefinition = "TINYINT")
    var answer: Int? = null,
)