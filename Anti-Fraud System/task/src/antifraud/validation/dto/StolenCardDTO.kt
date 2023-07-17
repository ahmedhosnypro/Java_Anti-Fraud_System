package antifraud.validation.dto

import antifraud.validation.model.StolenCard
import antifraud.validation.util.CardNumberConstraint
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.modelmapper.ModelMapper


class StolenCardDTO(
    var id: Long? = null,
    @NotNull @NotBlank @NotEmpty @Size(min = 16, max = 16)
    @CardNumberConstraint
    var number: String? = null,
) {
    companion object {
        fun toEntity(stolenCardDTO: StolenCardDTO): StolenCard =
            ModelMapper().map(stolenCardDTO, StolenCard::class.java)

        fun fromEntity(stolenCard: StolenCard): StolenCardDTO = ModelMapper().map(stolenCard, StolenCardDTO::class.java)
    }
}