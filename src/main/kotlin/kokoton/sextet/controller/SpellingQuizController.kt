package kokoton.sextet.controller

import kokoton.sextet.dto.ErrorResponseDTO
import kokoton.sextet.dto.SpellingQuizAnswerRequestDTO
import kokoton.sextet.service.SpellingQuizService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/quiz")
class SpellingQuizController(
    private val spellingQuizService: SpellingQuizService
) {

    @GetMapping
    fun getQuiz(
        @RequestParam(required = false) quiz_id: Long?
    ): ResponseEntity<Any> {
        return try {
            if (quiz_id == null) {
                // 랜덤 퀴즈 5개 가져오기
                val quizzes = spellingQuizService.getRandomQuizzes(5)
                ResponseEntity.ok(quizzes)
            } else {
                // 특정 퀴즈 가져오기
                val quiz = spellingQuizService.getQuizById(quiz_id)
                ResponseEntity.ok(quiz)
            }
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(
                ErrorResponseDTO(message = "quiz_index를 존재하는 범위 내에서 요청해주세요.", errorCode = 400)
            )
        } catch (e: Exception) {
            ResponseEntity.status(500).body(
                ErrorResponseDTO(message = "서버 처리 중 에러가 발생했습니다.", errorCode = 500)
            )
        }
    }

    // 정답 제출 API
    @PostMapping("/answer")
    fun submitAnswer(
        @RequestBody request: SpellingQuizAnswerRequestDTO
    ): ResponseEntity<Any> {
        return try {
            if (request.quiz_id <= 0 || request.user_choice < 0) {
                // 유효하지 않은 요청인 경우
                ResponseEntity.badRequest().body(
                    ErrorResponseDTO("유효한 퀴즈 id, user_choice을 전송해주세요.", 400)
                )
            } else {
                // 정답 확인 로직 호출
                val response = spellingQuizService.checkAnswer(request)
                ResponseEntity.ok(response)
            }
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(
                ErrorResponseDTO("유효한 퀴즈 id를 전송해주세요.", 400)
            )
        } catch (e: Exception) {
            ResponseEntity.status(500).body(
                ErrorResponseDTO("DB 처리 중 에러가 발생했습니다.", 500)
            )
        }
    }
}
