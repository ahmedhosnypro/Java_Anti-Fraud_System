package antifraud.controller

import antifraud.dto.CardDTO
import antifraud.service.CardService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class StolenCardController(
    private val cardService: CardService,
) {
    @PostMapping("/api/antifraud/stolencard")
    fun addStolenCard(@RequestBody @Valid cardDTO: CardDTO): ResponseEntity<Any> {
        if (cardDTO.number?.let { cardService.existByNumber(it) } == true) {
            return ResponseEntity.status(409).body("card already exists")
        }

        return ResponseEntity.ok(
            CardDTO.fromEntity(cardService.save(CardDTO.toEntity(cardDTO)))
        )
    }

    @GetMapping("/api/antifraud/stolencard")
    fun listStolenCards(): ResponseEntity<Any> =
        ResponseEntity.ok(cardService.findAll().sortedBy { it.id }.map { CardDTO.fromEntity(it) })


    @DeleteMapping("/api/antifraud/stolencard/{number}")
    fun deleteCard(
        @NotNull @NotBlank @NotEmpty @Size(min = 16, max = 16) @PathVariable number: String
    ): ResponseEntity<Any> {
        val card = cardService.findByNumber(number)
        if (card != null) {
            cardService.delete(card)
            return ResponseEntity.ok().body(
                mapOf("status" to "Card ${card.number} successfully removed!")
            )
        }
        return ResponseEntity.notFound().build()
    }
}