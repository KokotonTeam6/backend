package kokoton.sextet.model

import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProfileRepository : JpaRepository<Profile, Long> {
    @Transactional
    fun findByUsername(username: String): Profile?

    // 경험치 내림차순으로 프로필 목록을 페이지네이션 처리해서 가져옴
    @Query("SELECT p FROM Profile p ORDER BY p.xp DESC")
    fun findAllByOrderByXpDesc(pageable: Pageable): Page<Profile>
}
