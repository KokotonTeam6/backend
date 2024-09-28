package kokoton.sextet.service

import kokoton.sextet.dto.WrongNoteResponseDTO
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
        val wrongNotes = spellingAnswerNoteRepository.findAllByUserId(userId)
            .filter { note -> note.answer != note.quiz?.answer }
            .sortedByDescending { it.createdAt }
            .take(count)  // 요청한 개수만큼 제한, 없으면 최대 개수 50개 리턴

        // 오답노트 리스트 변환
        return wrongNotes.map { note ->
            WrongNoteResponseDTO(
                quizId = note.quiz?.id ?: 0,
                options = note.quiz?.options?.split(",") ?: emptyList(),
                question = note.quiz?.question ?: "",
                answerIndex = note.quiz?.answer ?: 0
            )
        }
    }
}
