package antifraud.service

import antifraud.model.Transaction
import antifraud.repository.TransactionRepository
import org.springframework.stereotype.Service
import java.sql.Timestamp
import kotlin.jvm.optionals.getOrNull

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository
) {
    fun save(transaction: Transaction) = transactionRepository.save(transaction)

    fun countRegions(transaction: Transaction): Long {
        val hourAgo = Timestamp.valueOf(transaction.date!!.toLocalDateTime().minusHours(1))
        return transactionRepository.countRegions(
            number = transaction.number!!, transactionDate = transaction.date!!, hourAgo = hourAgo
        )
    }

    fun countIps(transaction: Transaction): Long {
        val hourAgo = Timestamp.valueOf(transaction.date!!.toLocalDateTime().minusHours(1))
        return transactionRepository.countIps(
            number = transaction.number!!, transactionDate = transaction.date!!, hourAgo = hourAgo
        )
    }

    fun findAll(): MutableList<Transaction> = transactionRepository.findAll()
    fun existById(id: Long): Boolean = transactionRepository.existsById(id)
    fun findById(transactionId: Long) = transactionRepository.findById(transactionId).getOrNull()
    fun findAllByNumber(number: String) = transactionRepository.findAllByNumber(number)
}