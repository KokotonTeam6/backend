package kokoton.sextet.service

import kokoton.sextet.dto.RankListResponseDTO
import kokoton.sextet.dto.RankPercentResponseDTO
import kokoton.sextet.dto.UserRankDTO
import kokoton.sextet.model.ProfileRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.data.domain.Pageable

@Service
class RankService(
    private val profileRepository: ProfileRepository
) {

    // 상위 몇 퍼센트인지 계산하는 메서드
    fun getUserRankPercent(userId: Long): RankPercentResponseDTO {
        // 모든 유저를 경험치 내림차순으로 가져오기
        val allProfiles = profileRepository.findAllByOrderByXpDesc(Pageable.unpaged())

        // 전체 유저 수
        val totalUsers = allProfiles.size.toDouble()

        // 해당 유저의 순위를 구하기
        val userRank = allProfiles.indexOfFirst { profile -> profile.id == userId } + 1
        if (userRank <= 0) throw IllegalArgumentException("User not found")

        // 상위 몇 퍼센트인지 계산
        val rankPercent = (userRank / totalUsers) * 100.0

        return RankPercentResponseDTO(rank_percent = rankPercent)
    }

    // 랭킹 리스트를 페이지별로 가져오는 메서드
    fun getRankList(page: Int, size: Int): RankListResponseDTO {
        // PageRequest를 생성하여 페이지와 페이지 당 사이즈 설정
        val pageable = PageRequest.of(page - 1, size)  // 페이지는 0부터 시작하므로 -1 처리

        // 데이터베이스에서 페이지 단위로 프로필 목록 가져오기
        val profilePage = profileRepository.findAllByOrderByXpDesc(pageable)

        // 사용자 순위 목록 생성
        val userRanks = profilePage.content.mapIndexed { index, profile ->
            UserRankDTO(
                rank = (index + 1) + ((page - 1) * size),  // 페이지에 맞춰 순위 계산
                name = profile.nickname ?: "Unknown",
                exp = profile.xp
            )
        }

        return RankListResponseDTO(
            page = page,
            users = userRanks
        )
    }
}
