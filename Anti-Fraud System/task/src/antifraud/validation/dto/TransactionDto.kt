package antifraud.validation.dto

import antifraud.validation.model.Transaction
import antifraud.validation.util.CardNumberConstraint
import jakarta.validation.constraints.*
import org.modelmapper.ModelMapper

class TransactionDto(
    @NotNull @NotBlank @NotEmpty var amount: Int,

    @NotNull @NotBlank @NotEmpty @Pattern(regexp = """(\b25[0-5]|\b2[0-4][0-9]|\b[01]?[0-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}""") var ip: String? = null,

    @NotNull @NotBlank @NotEmpty @Size(min = 16, max = 16)
    @CardNumberConstraint
    var number: String? = null,
) {
    companion object {
        fun fromEntity(transaction: Transaction): TransactionDto =
            ModelMapper().map(transaction, TransactionDto::class.java)

        fun toEntity(transactionDto: TransactionDto): Transaction =
            ModelMapper().map(transactionDto, Transaction::class.java)
    }
}