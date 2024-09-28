package kokoton.sextet.controller

import kokoton.sextet.dto.RankListResponseDTO
import kokoton.sextet.dto.RankPercentResponseDTO
import kokoton.sextet.service.RankService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rank")
class RankController(
    private val rankService: RankService
) {

    // 순위 상위 몇 퍼센트인지 계산하는 API
    @GetMapping("/top-percent")
    fun getUserRankPercent(
        @RequestHeader("Authorization") accessToken: String  // JWT 토큰을 헤더에서 받음
    ): ResponseEntity<RankPercentResponseDTO> {
        // JWT 토큰에서 사용자 ID를 추출하는 로직
        val userId = extractUserIdFromToken(accessToken)

        return try {
            val rankResponse = rankService.getUserRankPercent(userId)
            ResponseEntity.ok(rankResponse)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            ResponseEntity.status(500).build()
        }
    }

    // JWT에서 사용자 ID 추출하는 함수 (구현 필요)
    private fun extractUserIdFromToken(token: String): Long {
        // 실제 JWT 파싱 로직 필요
        return 1L  // 예시로 사용자 ID를 1로 반환
    }

    // 랭킹 리스트를 페이지네이션하여 가져오는 API
    @GetMapping("/list")
    fun getRankList(
        @RequestHeader("Authorization") accessToken: String,  // JWT 토큰을 헤더에서 받음
        @RequestParam page: Int,  // 요청된 페이지 번호
        @RequestParam size: Int = 10  // 기본 페이지 사이즈는 10
    ): ResponseEntity<RankListResponseDTO> {
        return try {
            val rankList = rankService.getRankList(page, size)
            ResponseEntity.ok(rankList)
        } catch (e: Exception) {
            ResponseEntity.status(500).build()
        }
    }

}
