package antifraud.service

import antifraud.model.Transaction
import antifraud.repository.TransactionRepository
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository
) {
    fun save(transaction: Transaction) = transactionRepository.save(transaction)

    fun countRegions(transaction: Transaction): Long {
        val hourAgo = Timestamp.valueOf(transaction.date!!.toLocalDateTime().minusHours(1))
        return transactionRepository.countRegions(
            number = transaction.number!!,
            transactionDate = transaction.date!!,
            hourAgo = hourAgo
        )
    }

    fun countIps(transaction: Transaction): Long {
        val hourAgo = Timestamp.valueOf(transaction.date!!.toLocalDateTime().minusHours(1))
        return transactionRepository.countIps(
            number = transaction.number!!,
            transactionDate = transaction.date!!,
            hourAgo = hourAgo
        )
    }
}