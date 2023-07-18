package antifraud.model

import antifraud.util.RegionSet
import antifraud.util.TransactionValidationState
import antifraud.util.TransactionValidationState.*
import jakarta.persistence.*
import java.sql.Timestamp

@Entity(name = "Transactions")
class Transaction(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id", nullable = false) var id: Long? = null,
    @Column(nullable = false) var amount: Int? = null,
    @Column(nullable = false) var ip: String? = null,
    @Column(nullable = false) var number: String? = null,

    @Enumerated(EnumType.STRING) @Column(nullable = false) var region: RegionSet? = null,
    @Temporal(TemporalType.TIMESTAMP) @Column(nullable = false) var date: Timestamp? = null,

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