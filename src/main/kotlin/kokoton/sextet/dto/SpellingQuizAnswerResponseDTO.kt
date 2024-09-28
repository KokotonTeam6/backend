package kokoton.sextet.dto

data class SpellingQuizAnswerResponseDTO(
    val answer: Int,        // 정답 선택지 index
    val isCorrect: Boolean  // 사용자 선택이 정답인지 여부
)
