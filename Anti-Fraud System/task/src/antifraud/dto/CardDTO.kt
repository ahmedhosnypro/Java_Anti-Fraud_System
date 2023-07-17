package antifraud.dto

import antifraud.model.Card
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.modelmapper.ModelMapper


data class CardDTO(
    var id: Long? = null,

    @field:NotNull @field:NotBlank @field:NotEmpty @field:Size(min = 16, max = 16)
    var number: String? = null,
) {
    companion object {
        fun toEntity(cardDTO: CardDTO): Card =
            ModelMapper().map(cardDTO, Card::class.java)

        fun fromEntity(card: Card): CardDTO = ModelMapper().map(card, CardDTO::class.java)
    }
}