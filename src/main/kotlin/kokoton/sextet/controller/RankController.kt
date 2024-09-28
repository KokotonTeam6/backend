package kokoton.sextet.controller

import kokoton.sextet.dto.RankListResponseDTO
import kokoton.sextet.dto.RankPercentResponseDTO
import kokoton.sextet.service.RankService
import kokoton.sextet.util.getCurrentUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/rank")
class RankController(
    @Autowired private val rankService: RankService
) {

    // 순위 상위 몇 퍼센트인지 계산하는 API
    @GetMapping("/top-percent")
    fun getUserRankPercent(
    ): ResponseEntity<RankPercentResponseDTO> {
        // JWT 토큰에서 사용자 ID를 추출하는 로직
        val user = getCurrentUser()

        return try {
            val rankResponse = rankService.getUserRankPercent(user.id!!)
            ResponseEntity.ok(rankResponse)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            ResponseEntity.status(500).build()
        }
    }


    // 랭킹 리스트를 페이지네이션하여 가져오는 API
    @GetMapping("/list")
    fun getRankList(
        @RequestParam page: Int?,  // 요청된 페이지 번호. 기본은 1
        @RequestParam size: Int?  // 기본 페이지 사이즈는 10
    ): ResponseEntity<RankListResponseDTO> {
        return try {
            val rankList = rankService.getRankList(page ?: 1, size ?: 10)
            ResponseEntity.ok(rankList)
        } catch (e: Exception) {
            ResponseEntity.status(500).build()
        }
    }

}
