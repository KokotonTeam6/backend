package kokoton.sextet.controller

import kokoton.sextet.dto.WiseResponseDTO
import kokoton.sextet.model.WiseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/wise")
class WiseController(
    @Autowired private val wiseRepository: WiseRepository
) {
    @GetMapping("/get")
    fun getWise(): ResponseEntity<WiseResponseDTO> {
        val wise = wiseRepository.getTodayWise() ?: wiseRepository.createTodayWise()

        return ResponseEntity.ok(WiseResponseDTO(
            wise.id!!,
            wise.content!!,
            wise.author!!
        ))
    }
}