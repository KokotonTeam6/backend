package kokoton.sextet

import kokoton.sextet.dto.ErrorResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(SpellingException::class)
    fun handleException(ex: SpellingException): ResponseEntity<ErrorResponseDTO> {
        val response = ErrorResponseDTO(
            ex.message ?: "",
            ex.statusCode
        )

        return ResponseEntity.status(ex.statusCode).body(response)
    }
}

class SpellingException(
    message: String,
    val statusCode: Int = HttpStatus.EXPECTATION_FAILED.value()
) : RuntimeException(message)