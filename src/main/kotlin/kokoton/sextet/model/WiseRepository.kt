package kokoton.sextet.model

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface WiseRepository: JpaRepository<Wise, Long> {
    @Transactional
    @Query("SELECT w FROM Wise w WHERE DATE(w.lastShown) = CURRENT_DATE")
    fun getTodayWise(): Wise?
}