package antifraud.controller

import antifraud.dto.TransactionDTO
import antifraud.util.TransactionValidationState.*
import antifraud.model.Transaction
import antifraud.service.CardService
import antifraud.service.IpService
import antifraud.service.TransactionService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Validated
class TransactionController(
    private val ipService: IpService,
    private val cardService: CardService,
    private val transactionService: TransactionService
) {
    data class ValidationResult(val result: String, val info: String)

    @PostMapping("/api/antifraud/transaction")
    fun validateTransaction(@RequestBody @Valid transactionDto: TransactionDTO): ResponseEntity<Any> {
        val transaction = TransactionDTO.toEntity(transactionDto)
        transactionService.save(transaction)
        val invalidReasons = mutableListOf<String>()

        var transactionState = ALLOWED

        val suspiciousIp = transaction.ip?.let { ipService.findByIp(it) }
        if (suspiciousIp != null) {
            transactionState = PROHIBITED
            invalidReasons.add("ip")
        }

        val stolenCard = cardService.findByNumber(transaction.number!!)
        if (stolenCard != null) {
            transactionState = PROHIBITED
            invalidReasons.add("card-number")
        }


        val regions = transactionService.countRegions(transaction)
        if (regions > 3) {
            transactionState = PROHIBITED
            invalidReasons.add("region-correlation")
        } else if (regions.toInt() == 3 && transactionState != PROHIBITED) {
            transactionState = MANUAL_PROCESSING
            invalidReasons.add("region-correlation")
        }

        val ips = transactionService.countIps(transaction)
        if (ips > 3) {
            transactionState = PROHIBITED
            invalidReasons.add("ip-correlation")
        } else if (ips.toInt() == 3 && transactionState != PROHIBITED) {
            transactionState = MANUAL_PROCESSING
            invalidReasons.add("ip-correlation")
        }


        val amountValidationState = Transaction.validateAmount(transaction.amount)
        if (amountValidationState == PROHIBITED) {
            transactionState = PROHIBITED
            invalidReasons.add("amount")
        } else if (amountValidationState == MANUAL_PROCESSING && transactionState != PROHIBITED) {
            transactionState = MANUAL_PROCESSING
            invalidReasons.add("amount")
        }

        transactionService.save(transaction)

        return ResponseEntity.ok(
            ValidationResult(
                transactionState.toString(),
                if (transactionState == ALLOWED) "none" else invalidReasons.apply { sort() }.joinToString(", ")
            )
        )
    }
}

