package antifraud.validation.controller

import antifraud.validation.TransactionValidationState.*
import antifraud.validation.model.Transaction
import antifraud.validation.service.StolenCardService
import antifraud.validation.service.SuspiciousIpService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class AntifraudController(
    private val suspiciousIpService: SuspiciousIpService,
    private val stolenCardService: StolenCardService,
) {
    data class ValidationResult(val result: String, val info: String)

    @PostMapping("/api/antifraud/transaction")
    fun validateTransaction(
        @RequestBody transaction: Transaction
    ): ResponseEntity<Any> {
        if (transaction.amount!! < 1) {
            return ResponseEntity.badRequest().body("amount must be greater than 0")
        }

        val invalidReasons = mutableListOf<String>()

        var transactionState = ALLOWED

        val suspiciousIp = suspiciousIpService.findByIp(transaction.ip!!)
        if (suspiciousIp == null) {
            transactionState = PROHIBITED
            invalidReasons.add("ip")
        }

        val stolenCard = stolenCardService.findByNumber(transaction.number!!)
        if (stolenCard == null) {
            transactionState = PROHIBITED
            invalidReasons.add("number")
        }

        val amountValidationState = Transaction.validateAmount(transaction)
        if (amountValidationState == PROHIBITED) {
            transactionState = PROHIBITED
            invalidReasons.add("amount")
        } else if (amountValidationState == MANUAL_PROCESSING) {
            transactionState = if (transactionState == PROHIBITED) PROHIBITED else MANUAL_PROCESSING
            invalidReasons.add("amount")
        }

        return ResponseEntity.ok(
            ValidationResult(
                transactionState.toString(),
                if (transactionState == ALLOWED) "" else invalidReasons.joinToString(", ")
            )
        )
    }




}

