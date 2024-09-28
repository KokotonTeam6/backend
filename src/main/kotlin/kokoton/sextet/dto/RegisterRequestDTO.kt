package kokoton.sextet.dto

data class RegisterRequestDTO(
    val username: String,
    val password: String,
    val nickname: String
)