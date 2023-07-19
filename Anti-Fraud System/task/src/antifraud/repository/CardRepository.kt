package antifraud.repository

import antifraud.model.Card
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface CardRepository : JpaRepository<Card, Long> {
    fun findByNumber(number: String): Optional<Card>
    fun existsByNumber(number: String): Boolean
    fun findAllByStolenTrue(): MutableList<Card>
}