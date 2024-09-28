package kokoton.sextet.model

import org.springframework.data.jpa.repository.JpaRepository

interface EmailingProfessorRepository: JpaRepository<EmailingProfessor, Long> {
}