package antifraud.dto

import antifraud.model.Transaction
import antifraud.util.*
import jakarta.validation.constraints.*
import org.modelmapper.ModelMapper
import java.time.LocalDateTime

data class TransactionDTO(
    var transactionId: Long? = null,
    @field:NotNull @field:Positive var amount: Int? = null,
    @field:Pattern(regexp = IpAddressUtil.IPV4_REGEX) var ip: String? = null,
    @field:CardNumberConstraint var number: String? = null,
    @field:RegionConstraint var region: String? = null,
    @field:NotNull @field:Past var date: LocalDateTime? = null,
    var result: String? = null,
    var feedback: String? = null,
) {
    companion object {
        fun fromEntity(transaction: Transaction): TransactionDTO =
            ModelMapper().map(transaction, TransactionDTO::class.java).apply {
                this.transactionId = transaction.id
                this.region = transaction.region?.name
                this.date = transaction.date?.toLocalDateTime()
                this.result = transaction.result?.name
                this.feedback = if (transaction.feedback == null ) "" else transaction.feedback!!.name
            }
    }
}