package kokoton.sextet.controller

import kokoton.sextet.SpellingException
import kokoton.sextet.dto.WrongNoteResponseDTO
import kokoton.sextet.service.WrongNoteService
import kokoton.sextet.util.getCurrentUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/wrong-notes")
class WrongNoteController(
    @Autowired private val wrongNoteService: WrongNoteService
) {

    // 오답노트 리스트를 받아오는 API
    @GetMapping
    fun getWrongNotes(
        @RequestParam count: Int?  // nullable로 받음
    ): ResponseEntity<List<WrongNoteResponseDTO>> {
        return try {
            val user = getCurrentUser()

            // count가 null이면 50으로 처리
            val notes = wrongNoteService.getWrongNotes(user.id ?: 0L, count ?: 30)
            return ResponseEntity.ok(notes)
        } catch (e: IllegalArgumentException) {
            throw SpellingException("유효한 요청이 아닙니다.", 400)
        } catch (e: Exception) {
            throw SpellingException(message = "DB 처리 중 에러가 발생했습니다.", 500)
        }
    }

}
