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
    fun findAllByStolenTrue(): MutableList<Card> = cardRepository.findAllByStolenTrue()
    fun createIfNotFount(number: String): Card = findByNumber(number) ?: save(Card(number = number))
}