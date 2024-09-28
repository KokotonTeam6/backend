package kokoton.sextet.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SpellingAnswerNoteRepository : JpaRepository<SpellingAnswerNote, Long> {

    // 특정 유저와 퀴즈에 해당하는 답변이 이미 존재하는지 확인하는 메서드
    fun findByUserAndQuiz(user: Profile, quiz: SpellingQuiz): SpellingAnswerNote?

    // 특정 유저가 제출한 모든 답변을 찾는 메서드
    fun findAllByUserId(userId: Long): List<SpellingAnswerNote>
}
