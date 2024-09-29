package kokoton.sextet.service

import jakarta.transaction.Transactional
import kokoton.sextet.model.Wise
import kokoton.sextet.model.WiseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class WiseService(
    @Autowired private val wiseRepository: WiseRepository
) {
    @Transactional
    fun createTodayWise(): Wise {
        val today = LocalDate.now()

        val yesterday = today.minusDays(1)

        val candidates = wiseRepository.findAll().filter {
            it.lastShown?.toLocalDate()!! < yesterday
        }

        val newWise = candidates.randomOrNull() ?: throw RuntimeException("No available wise to select")
        newWise.lastShown = LocalDateTime.now()

        return newWise
    }
}