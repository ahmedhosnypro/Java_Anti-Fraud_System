package antifraud.service

import antifraud.model.Card
import antifraud.repository.CardRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class CardService(
    private val cardRepository: CardRepository,
) {
    fun save(card: Card): Card = cardRepository.save(card)
    fun findByNumber(number: String): Card? = cardRepository.findByNumber(number).getOrNull()
    fun existByNumber(number: String): Boolean = cardRepository.existsByNumber(number)

    fun delete(card: Card) = cardRepository.delete(card)
    fun findAll(): MutableList<Card> = cardRepository.findAll()
}