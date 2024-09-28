package kokoton.sextet.model

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.time.LocalDateTime

interface WiseRepository: JpaRepository<Wise, Long> {
    @Transactional
    @Query("SELECT w FROM Wise w WHERE DATE(w.lastShown) = CURRENT_DATE")
    fun getTodayWise(): Wise?

    @Transactional
    fun createTodayWise(): Wise {
        val today = LocalDate.now()

        val yesterday = today.minusDays(1)

        val candidates = findAll().filter {
            it.lastShown?.toLocalDate() != yesterday
        }

        val newWise = candidates.randomOrNull() ?: throw RuntimeException("No available wise to select")
        newWise.lastShown = LocalDateTime.now()

        return newWise
    }
}