package kokoton.sextet.dto

data class ProfileResponseDTO(
    val id: Long,
    val username: String,
    val nickname: String,
    val imageUrl: String,
    val xp: Int,
    val point: Int
)
