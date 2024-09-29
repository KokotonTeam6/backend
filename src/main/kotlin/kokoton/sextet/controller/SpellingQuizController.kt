package kokoton.sextet.controller

import kokoton.sextet.SpellingException
import kokoton.sextet.dto.SpellingQuizAnswerRequestDTO
import kokoton.sextet.dto.SpellingQuizAnswerResponseDTO
import kokoton.sextet.dto.SpellingQuizResponseDTO
import kokoton.sextet.service.SpellingQuizService
import kokoton.sextet.util.getCurrentUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/quiz")
class SpellingQuizController(
    @Autowired private val spellingQuizService: SpellingQuizService
) {

    @GetMapping
    fun getQuiz(
        @RequestParam(required = false) quizId: Long?
    ): ResponseEntity<List<SpellingQuizResponseDTO>> {
        return try {
            if (quizId == null) {
                // 랜덤 퀴즈 5개 가져오기
                val quizzes = spellingQuizService.getRandomQuizzes(5)
                ResponseEntity.ok(quizzes)
            } else {
                // 특정 퀴즈 가져오기
                val quiz = spellingQuizService.getQuizById(quizId)
                return ResponseEntity.ok(listOf(quiz))
            }
        } catch (e: IllegalArgumentException) {
            throw SpellingException("quiz_index를 존재하는 범위 내에서 요청해주세요.", 400)
        } catch (e: Exception) {
            throw SpellingException("서버 처리 중 에러가 발생했습니다.", 500)
        }
    }

    // 정답 제출 API
    @PostMapping("/answer")
    fun submitAnswer(
        @RequestBody request: SpellingQuizAnswerRequestDTO
    ): ResponseEntity<SpellingQuizAnswerResponseDTO> {
        return try {
            if (request.quiz_id <= 0 || request.user_choice < 0) {
                // 유효하지 않은 요청인 경우
                throw SpellingException("유효한 퀴즈 id, user_choice을 전송해주세요.", 400)
            } else {
                val user = getCurrentUser()
                // 정답 확인 로직 호출
                val response = spellingQuizService.submitAnswer(user, request)
                ResponseEntity.ok(response)
            }
        } catch (e: IllegalArgumentException) {
            throw SpellingException("유효한 퀴즈 id를 전송해주세요.", 400)
        } catch (e: Exception) {
            throw SpellingException("DB 처리 중 에러가 발생했습니다.", 500)
        }
    }
}
