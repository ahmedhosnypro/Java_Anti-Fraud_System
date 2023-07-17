package antifraud.validation.service

import antifraud.validation.model.StolenCard
import antifraud.validation.repository.StolenCardRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class StolenCardService(
    private val stolenCardRepository: StolenCardRepository,
) {
    fun save(stolenCard: StolenCard): StolenCard = stolenCardRepository.save(stolenCard)
    fun findByNumber(number: String): StolenCard? = stolenCardRepository.findByNumber(number).getOrNull()
    fun existByNumber(number: String): Boolean = stolenCardRepository.existsByNumber(number)

    fun delete(stolenCard: StolenCard) = stolenCardRepository.delete(stolenCard)
    fun findAll(): MutableList<StolenCard> = stolenCardRepository.findAll()
}