package antifraud.dto

import antifraud.model.Card
import antifraud.util.CardNumberConstraint
import org.modelmapper.ModelMapper


data class CardDTO(
    var id: Long? = null,
   @field:CardNumberConstraint var number: String? = null,
) {
    companion object {
        fun toEntity(cardDTO: CardDTO): Card =
            ModelMapper().map(cardDTO, Card::class.java)

        fun fromEntity(card: Card): CardDTO = ModelMapper().map(card, CardDTO::class.java)
    }
}