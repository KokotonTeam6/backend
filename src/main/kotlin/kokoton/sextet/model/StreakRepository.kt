package kokoton.sextet.model

import org.springframework.data.jpa.repository.JpaRepository

interface StreakRepository: JpaRepository<Streak, Long> {

}