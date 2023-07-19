package antifraud.dto

import antifraud.util.TransactionStateConstraint
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class TransactionFeedbackDto(
    @field:NotNull @field:Positive var transactionId: Long? = null,
    @field:TransactionStateConstraint var feedback: String? = null,
)