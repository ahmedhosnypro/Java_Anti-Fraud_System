package antifraud.dto

import antifraud.model.Card
import antifraud.util.CardNumberConstraint
import org.modelmapper.ModelMapper


data class StolenCardDtoCardDTO(
    var id: Long? = null,
    @field:CardNumberConstraint var number: String? = null,
) {
    companion object {
        fun toEntity(stolenCardDtoCardDTO: StolenCardDtoCardDTO): Card =
            ModelMapper().map(stolenCardDtoCardDTO, Card::class.java).apply {
                this.stolen = true
            }

        fun fromEntity(card: Card): StolenCardDtoCardDTO = ModelMapper().map(card, StolenCardDtoCardDTO::class.java)
    }
}