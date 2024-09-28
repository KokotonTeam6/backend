package kokoton.sextet.model

import org.springframework.data.jpa.repository.JpaRepository

interface SpellingAnswerNoteRepository: JpaRepository<SpellingAnswerNote, Long> {

    // 특정 유저가 답한 모든 기록을 가져오는 메서드
    fun findAllByUserId(userId: Long): List<SpellingAnswerNote>
}