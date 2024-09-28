package kokoton.sextet.controller

import kokoton.sextet.dto.ErrorResponseDTO
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
    ): ResponseEntity<Any> {
        return try {
            val user = getCurrentUser()

            // count가 null이면 50으로 처리
            val notes = wrongNoteService.getWrongNotes(user.id ?: 0L, count ?: 30)
            ResponseEntity.ok(notes)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(
                ErrorResponseDTO(message = "유효한 요청이 아닙니다.", errorCode = 400)
            )
        } catch (e: Exception) {
            ResponseEntity.status(500).body(
                ErrorResponseDTO(message = "DB 처리 중 에러가 발생했습니다.", errorCode = 500)
            )
        }
    }

}
