package antifraud.validation.model

import antifraud.validation.TransactionValidationState
import antifraud.validation.TransactionValidationState.*
import jakarta.persistence.*

@Entity
class Transaction(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "id", nullable = false) var id: Long? = null,
    @Column(name = "amount", nullable = false) var amount: Int? = null,
    @Column(name = "ip", nullable = false) var ip: String? = null,
    @Column(name = "number", nullable = false) var number: String? = null,
) {
    companion object {
        fun validateAmount(transaction: Transaction): TransactionValidationState {
            return when {
                transaction.amount!! < 1 -> INVALID
                transaction.amount in 1..200 -> ALLOWED
                transaction.amount in 201..1500 -> MANUAL_PROCESSING
                else -> PROHIBITED
            }
        }
    }
}