package kokoton.sextet.service

import kokoton.sextet.dto.SpellingQuizAnswerRequestDTO
import kokoton.sextet.dto.SpellingQuizAnswerResponseDTO
import kokoton.sextet.dto.SpellingQuizResponseDTO
import kokoton.sextet.model.Profile  // Profile import 추가
import kokoton.sextet.model.SpellingAnswerNote  // SpellingAnswerNote import 추가
import kokoton.sextet.model.SpellingAnswerNoteRepository  // SpellingAnswerNoteRepository import 추가
import kokoton.sextet.model.SpellingQuiz
import kokoton.sextet.model.SpellingQuizRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SpellingQuizService(
    @Autowired private val spellingQuizRepository: SpellingQuizRepository,
    @Autowired private val spellingAnswerNoteRepository: SpellingAnswerNoteRepository,  // repository 주입
    @Autowired private val profileService: ProfileService
) {

    // 특정 퀴즈를 가져오는 메서드
    fun getQuizById(quizId: Long): SpellingQuizResponseDTO {
        val quiz = spellingQuizRepository.findById(quizId)
            .orElseThrow { IllegalArgumentException("Invalid quiz_id") }
        return quiz.toDTO()
    }

    // 랜덤한 퀴즈를 가져오는 메서드
    fun getRandomQuizzes(limit: Int): List<SpellingQuizResponseDTO> {
        val quizzes = spellingQuizRepository.findRandomQuizzes(limit)
        return quizzes.map { it.toDTO() }
    }

    // SpellingQuiz 엔티티를 SpellingQuizResponseDTO로 변환하는 확장 함수
    private fun SpellingQuiz.toDTO(): SpellingQuizResponseDTO {
        return SpellingQuizResponseDTO(
            quizId = this.id?.toInt() ?: throw IllegalStateException("Quiz ID is missing"),
            question = this.question ?: throw IllegalStateException("Question is missing"),
            options = this.options?.split(",") ?: emptyList()
        )
    }

    // 사용자가 정답을 제출하면 저장하는 메서드
    fun submitAnswer(user: Profile, request: SpellingQuizAnswerRequestDTO): SpellingQuizAnswerResponseDTO {
        val quiz = spellingQuizRepository.findById(request.quiz_id.toLong())
            .orElseThrow { IllegalArgumentException("Invalid quiz_id") }

        // 퀴즈의 정답이 몇 번째 인덱스인지 파악
        val correctAnswerIndex = quiz.answer ?: throw IllegalStateException("Correct answer is missing")

        // 사용자가 선택한 답이 정답인지 확인
        val isCorrect = (request.user_choice == correctAnswerIndex)

        // 정답을 맞췄다면 XP 증가
        if (isCorrect) {
            profileService.increaseXp(user, score = 1)  // 맞출 경우 10 XP 증가
        }
        // 사용자가 선택한 답을 저장
        val answerNote = SpellingAnswerNote(
            user = user,
            quiz = quiz,
            answer = request.user_choice.toShort()
        )
        spellingAnswerNoteRepository.save(answerNote)  // 데이터베이스에 저장

        return SpellingQuizAnswerResponseDTO(
            answer = correctAnswerIndex,
            isCorrect = isCorrect
        )
    }
}
