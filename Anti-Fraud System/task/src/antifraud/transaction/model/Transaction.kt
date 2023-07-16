package antifraud.transaction.model

import antifraud.transaction.TransactionValidationState
import antifraud.transaction.TransactionValidationState.*
import com.fasterxml.jackson.annotation.JsonProperty

data class Transaction(
    @JsonProperty("amount")var amount: Int,
) {
    companion object {
        fun validate(transaction: Transaction): TransactionValidationState {
            return when {
                transaction.amount < 1 -> INVALID
                transaction.amount in 1..200 -> ALLOWED
                transaction.amount in 201..1500 -> MANUAL_PROCESSING
                else -> PROHIBITED
            }
        }
    }
}