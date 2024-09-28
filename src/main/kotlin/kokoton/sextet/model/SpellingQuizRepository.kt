package kokoton.sextet.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SpellingQuizRepository : JpaRepository<SpellingQuiz, Long> {

    // 네이티브 쿼리를 사용하여 랜덤하게 중복 없이 퀴즈를 LIMIT 수만큼 가져오는 쿼리
    @Query(value = "SELECT * FROM spelling_quiz ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    fun findRandomQuizzes(@org.springframework.data.repository.query.Param("limit") limit: Int): List<SpellingQuiz>
}
