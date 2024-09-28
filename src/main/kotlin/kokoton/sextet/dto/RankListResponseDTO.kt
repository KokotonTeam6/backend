package kokoton.sextet.dto

data class RankListResponseDTO(
    val page: Int,
    val users: List<UserRankDTO>
)
