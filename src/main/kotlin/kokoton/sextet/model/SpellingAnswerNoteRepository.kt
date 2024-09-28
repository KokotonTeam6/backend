package kokoton.sextet.model

import org.springframework.data.jpa.repository.JpaRepository

interface SpellingAnswerNoteRepository: JpaRepository<SpellingAnswerNote, Long> {
}