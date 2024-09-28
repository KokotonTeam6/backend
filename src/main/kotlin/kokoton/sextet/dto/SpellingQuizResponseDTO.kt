package kokoton.sextet.dto

data class SpellingQuizResponseDTO(
    val quizId: Int,
    val question: String,
    val options: List<String>
)
