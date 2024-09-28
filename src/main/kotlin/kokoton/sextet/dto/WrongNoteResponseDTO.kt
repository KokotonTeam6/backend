package kokoton.sextet.dto

data class WrongNoteResponseDTO(
    val quizId: Long,
    val options: List<String>,
    val question: String,
    val answerIndex: Int
)
