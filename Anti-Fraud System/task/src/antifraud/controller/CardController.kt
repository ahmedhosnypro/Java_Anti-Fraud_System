package antifraud.controller

import antifraud.dto.StolenCardDtoCardDTO
import antifraud.service.CardService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
@EnableMethodSecurity
@RequestMapping("/api/antifraud")
class CardController(
    private val cardService: CardService,
) {
    @PostMapping("/stolencard")
    @PreAuthorize("hasAuthority('ADD_STOLEN_CARD_PRIVILEGE')")
    fun addStolenCard(@RequestBody @Valid stolenCardDtoCardDTO: StolenCardDtoCardDTO): ResponseEntity<Any> {
        val card = cardService.findByNumber(stolenCardDtoCardDTO.number!!)
        if (card != null && card.stolen) {
            return ResponseEntity.status(409).body("card already exists")
        }

        if (card!=null){
            cardService.save(card.apply { stolen = true })
            return ResponseEntity.ok(
                StolenCardDtoCardDTO.fromEntity(card)
            )
        }
        return ResponseEntity.ok(
            StolenCardDtoCardDTO.fromEntity(cardService.save(StolenCardDtoCardDTO.toEntity(stolenCardDtoCardDTO)))
        )
    }

    @GetMapping("/stolencard")
    @PreAuthorize("hasAuthority('LIST_STOLEN_CARD_PRIVILEGE')")
    fun listStolenCards(): ResponseEntity<Any> =
        ResponseEntity.ok(cardService.findAllByStolenTrue().map { StolenCardDtoCardDTO.fromEntity(it) })


    @DeleteMapping("/stolencard/{number}")
    @PreAuthorize("hasAuthority('DELETE_STOLEN_CARD_PRIVILEGE')")
    fun editCardStolenState(
        @NotNull @NotBlank @NotEmpty @Size(min = 16, max = 16) @PathVariable number: String
    ): ResponseEntity<Any> {
        val card = cardService.findByNumber(number)
        if (card == null || !card.stolen) {
            return ResponseEntity.notFound().build()
        }
        cardService.save(card.apply { stolen = false })
        return ResponseEntity.ok().body(
            mapOf("status" to "Card ${card.number} successfully removed!")
        )
    }
}