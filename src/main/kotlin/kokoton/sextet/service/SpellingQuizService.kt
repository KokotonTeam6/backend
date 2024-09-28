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

    // 사용자가 정답을 제출하면 저장 또는 업데이트하는 메서드 (upsert)
    fun submitAnswer(user: Profile, request: SpellingQuizAnswerRequestDTO): SpellingQuizAnswerResponseDTO {
        val quiz = spellingQuizRepository.findById(request.quiz_id.toLong())
            .orElseThrow { IllegalArgumentException("Invalid quiz_id") }

        // 이미 동일한 퀴즈에 대한 답변이 존재하는지 확인
        val existingAnswerNote = spellingAnswerNoteRepository.findByUserAndQuiz(user, quiz)

        if (existingAnswerNote != null) {
            // 이미 존재하는 답이 있다면 업데이트 처리
            existingAnswerNote.answer = request.user_choice
            spellingAnswerNoteRepository.save(existingAnswerNote)
        } else {
            // 새로운 답을 저장
            val newAnswerNote = SpellingAnswerNote(
                user = user,
                quiz = quiz,
                answer = request.user_choice
            )
            spellingAnswerNoteRepository.save(newAnswerNote)
        }

        // 퀴즈의 정답이 몇 번째 인덱스인지 파악
        val correctAnswerIndex = quiz.answer ?: throw IllegalStateException("Correct answer is missing")

        // 사용자가 선택한 답이 정답인지 확인
        val isCorrect = (request.user_choice == correctAnswerIndex)

        // 정답을 맞췄다면 XP 증가
        if (isCorrect) {
            profileService.increaseXp(user, score = 1)  // 맞출 경우 1 XP 증가
        }

        return SpellingQuizAnswerResponseDTO(
            answer = correctAnswerIndex,
            isCorrect = isCorrect
        )
    }
}
