package antifraud.model

import antifraud.util.RegionSet
import antifraud.util.TransactionState
import antifraud.util.TransactionState.*
import jakarta.persistence.*
import java.sql.Timestamp
import kotlin.math.ceil

@Entity(name = "Transactions")
class Transaction(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id", nullable = false) var id: Long? = null,
    @Column(nullable = false) var amount: Int? = null,
    @Column(nullable = false) var ip: String? = null,
    @Column(nullable = false) var number: String? = null,

    @Enumerated(EnumType.STRING) @Column(nullable = false) var region: RegionSet? = null,
    @Temporal(TemporalType.TIMESTAMP) @Column(nullable = false) var date: Timestamp? = null,
    @Enumerated(EnumType.STRING) @Column(nullable = true) var result: TransactionState? = null,
    @Enumerated(EnumType.STRING) @Column(nullable = true) var feedback: TransactionState? = null,

    ) {
    companion object {
        fun validateAmount(transaction: Transaction, card: Card): TransactionState {
            return when {
                transaction.amount!! < 1 -> INVALID
                transaction.amount in 1..card.maxAllowed -> ALLOWED
                transaction.amount in card.maxAllowed ..card.maxManualProcessing -> MANUAL_PROCESSING
                else -> PROHIBITED
            }
        }
    }
}