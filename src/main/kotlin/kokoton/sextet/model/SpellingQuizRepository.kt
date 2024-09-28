package kokoton.sextet.model

import org.springframework.data.jpa.repository.JpaRepository

interface SpellingQuizRepository: JpaRepository<SpellingQuiz, Long> {

}