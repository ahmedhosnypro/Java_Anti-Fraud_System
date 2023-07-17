package antifraud.validation.repository

import antifraud.validation.model.StolenCard
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface StolenCardRepository : JpaRepository<StolenCard, Long> {
    fun findByNumber(number: String): Optional<StolenCard>
    fun existsByNumber(number: String): Boolean
}