package kokoton.sextet.service

import kokoton.sextet.dto.WrongNoteResponseDTO
import kokoton.sextet.model.SpellingAnswerNote
import kokoton.sextet.model.SpellingAnswerNoteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class WrongNoteService(
    @Autowired private val spellingAnswerNoteRepository: SpellingAnswerNoteRepository
) {

    // 오답노트를 받아오는 메서드
    fun getWrongNotes(userId: Long, count: Int): List<WrongNoteResponseDTO> {
        // 사용자가 제출한 답변 중 오답만 찾기, 최신순으로 정렬
        val wrongNotes: List<SpellingAnswerNote> = spellingAnswerNoteRepository.findAllByUserId(userId)
            .filter { note: SpellingAnswerNote -> note.answer != note.quiz?.answer }
            .sortedByDescending { note: SpellingAnswerNote -> note.createdAt }
            .take(count)

        // 오답노트 리스트 변환
        return wrongNotes.map { note: SpellingAnswerNote ->
            WrongNoteResponseDTO(
                quizId = note.quiz?.id ?: 0,
                options = note.quiz?.options?.split(",") ?: emptyList(),
                question = note.quiz?.question ?: "",
                answerIndex = note.quiz?.answer ?: 0
            )
        }
    }
}
