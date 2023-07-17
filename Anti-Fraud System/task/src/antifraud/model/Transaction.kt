package antifraud.model

import antifraud.util.TransactionValidationState
import antifraud.util.TransactionValidationState.*
import jakarta.persistence.*

@Entity
class Transaction(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id", nullable = false) var id: Long? = null,
    @Column(name = "amount", nullable = false) var amount: Int? = null,
    @Column(name = "ip", nullable = false) var ip: String? = null,
    @Column(name = "number", nullable = false) var number: String? = null,
) {
    companion object {
        fun validateAmount(amount: Int?): TransactionValidationState {
            return when {
                amount!! < 1 -> INVALID
                amount in 1..200 -> ALLOWED
                amount in 201..1500 -> MANUAL_PROCESSING
                else -> PROHIBITED
            }
        }
    }
}