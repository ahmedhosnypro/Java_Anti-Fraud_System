package antifraud.dto

import antifraud.model.Transaction
import antifraud.util.CardNumberConstraint
import antifraud.util.IpAddressUtil
import antifraud.util.RegionConstraint
import antifraud.util.RegionSet
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import org.modelmapper.ModelMapper
import java.sql.Timestamp

class TransactionValidationDTO (
    @field:NotNull @field:Positive var amount: Int? = null,
    @field:Pattern(regexp = IpAddressUtil.IPV4_REGEX) var ip: String? = null,
    @field:CardNumberConstraint var number: String? = null,
    @field:RegionConstraint var region: String? = null,
    @field:NotNull @field:Past var date: Timestamp? = null,
){
    companion object{
        fun toEntity(transactionValidationDTO: TransactionValidationDTO): Transaction {
            return ModelMapper().map(
               transactionValidationDTO, Transaction::class.java
            ).apply {
                this.region = transactionValidationDTO.region?.let { RegionSet.valueOf(it) }
            }
        }
    }
}