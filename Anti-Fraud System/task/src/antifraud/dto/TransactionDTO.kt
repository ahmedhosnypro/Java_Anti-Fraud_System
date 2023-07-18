package antifraud.dto

import antifraud.model.Transaction
import antifraud.util.CardNumberConstraint
import antifraud.util.IpAddressUtil
import antifraud.util.RegionConstraint
import antifraud.util.RegionSet
import jakarta.validation.constraints.*
import org.modelmapper.ModelMapper
import java.sql.Timestamp

data class TransactionDTO(
    @field:NotNull @field:Positive var amount: Int? = null,

    @field:NotBlank @field:Pattern(regexp = IpAddressUtil.IPV4_REGEX) var ip: String? = null,

    @field:NotBlank @field:Size(min = 16, max = 16) @field:CardNumberConstraint var number: String? = null,

    @field:NotBlank @field:Size(min = 2, max = 4) @field:RegionConstraint var region: String? = null,

    @field:NotNull @field:Past var date: Timestamp? = null,
) {
    companion object {
        fun toEntity(transactionDTO: TransactionDTO): Transaction {
            return ModelMapper().map(transactionDTO, Transaction::class.java).apply {
                this.region = transactionDTO.region?.let { RegionSet.valueOf(it) }
            }
        }
    }
}