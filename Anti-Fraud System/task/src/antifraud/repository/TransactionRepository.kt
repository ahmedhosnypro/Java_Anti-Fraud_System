package antifraud.repository

import antifraud.model.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.sql.Timestamp

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {
    @Query(
        "SELECT COUNT(DISTINCT t.region) FROM transactions t WHERE t.number = :number AND t.date BETWEEN :hourAgo AND :transactionDate",
        nativeQuery = true
    )
    fun countRegions(number: String, transactionDate: Timestamp, hourAgo: Timestamp): Long

    @Query(
        "SELECT COUNT(DISTINCT t.ip) FROM Transactions t WHERE t.number = :number AND t.date BETWEEN :hourAgo AND :transactionDate",
        nativeQuery = true
    )
    fun countIps(number: String, transactionDate: Timestamp, hourAgo: Timestamp): Long
}