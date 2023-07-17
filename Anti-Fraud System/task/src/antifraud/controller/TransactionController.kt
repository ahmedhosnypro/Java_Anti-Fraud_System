package antifraud.controller

import antifraud.dto.TransactionDto
import antifraud.util.TransactionValidationState.*
import antifraud.model.Transaction
import antifraud.service.CardService
import antifraud.service.IpService
import antifraud.util.CardNumberValidator
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class TransactionController(
    private val ipService: IpService,
    private val cardService: CardService,
) {
    data class ValidationResult(val result: String, val info: String)

    @PostMapping("/api/antifraud/transaction")
    fun validateTransaction(
        @RequestBody transactionDto: TransactionDto
    ): ResponseEntity<Any> {
        if (transactionDto.amount < 1) {
            return ResponseEntity.badRequest().body("amount must be greater than 0")
        }

        if (transactionDto.number?.let { CardNumberValidator.isValid(it) } == false) {
            return ResponseEntity.badRequest().body("invalid card number")
        }

        val invalidReasons = mutableListOf<String>()

        var transactionState = ALLOWED

        val suspiciousIp = transactionDto.ip?.let { ipService.findByIp(it) }
        if (suspiciousIp != null) {
            transactionState = PROHIBITED
            invalidReasons.add("ip")
        }

        val stolenCard = cardService.findByNumber(transactionDto.number!!)
        if (stolenCard != null) {
            transactionState = PROHIBITED
            invalidReasons.add("card-number")
        }

        val amountValidationState = Transaction.validateAmount(transactionDto.amount)
        if (amountValidationState == PROHIBITED) {
            transactionState = PROHIBITED
            invalidReasons.add("amount")
        } else if (amountValidationState == MANUAL_PROCESSING && transactionState != PROHIBITED) {
            transactionState = MANUAL_PROCESSING
            invalidReasons.add("amount")
        }

        return ResponseEntity.ok(
            ValidationResult(
                transactionState.toString(),
                if (transactionState == ALLOWED) "none" else invalidReasons.apply { sort() }.joinToString(", ")
            )
        )
    }
}

