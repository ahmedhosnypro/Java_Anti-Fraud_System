package antifraud.validation.controller

import antifraud.validation.dto.StolenCardDTO
import antifraud.validation.service.StolenCardService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class StolenCardController(
    private val stolenCardService: StolenCardService,
) {
    @PostMapping("/api/antifraud/stolencard")
    fun addStolenCard(@RequestBody stolenCardDTO: StolenCardDTO): ResponseEntity<Any> {
        if (stolenCardDTO.number?.let { stolenCardService.existByNumber(it) } == true) {
            return ResponseEntity.status(409).body("card already exists")
        }

        return ResponseEntity.created(URI.create("/api/antifraud/stolencard")).body(
            stolenCardService.save(StolenCardDTO.toEntity(stolenCardDTO))
        )
    }

    @GetMapping("/api/antifraud/stolencard")
    fun listStolenCards(): ResponseEntity<Any> = ResponseEntity.ok(stolenCardService.findAll().sortedBy { it.id })

}