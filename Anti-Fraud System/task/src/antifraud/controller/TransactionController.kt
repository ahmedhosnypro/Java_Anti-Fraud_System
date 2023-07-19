package antifraud.controller

import antifraud.dto.TransactionDTO
import antifraud.dto.TransactionFeedbackDto
import antifraud.dto.TransactionValidationDTO
import antifraud.model.Transaction
import antifraud.service.CardService
import antifraud.service.IpService
import antifraud.service.TransactionService
import antifraud.util.CardNumberConstraint
import antifraud.util.TransactionState
import antifraud.util.TransactionState.*
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.math.ceil


@RestController
@Validated
@EnableMethodSecurity(prePostEnabled = true)
@RequestMapping("/api/antifraud")
class TransactionController(
    private val ipService: IpService,
    private val cardService: CardService,
    private val transactionService: TransactionService
) {
    data class ValidationResult(val result: String, val info: String)

    @PostMapping("/transaction")
    @PreAuthorize("hasAuthority('CHECK_TRANSACTION_PRIVILEGE')")
    fun validateTransaction(@RequestBody @Valid transactionValidationDTO: TransactionValidationDTO): ResponseEntity<Any> {
        val transaction = TransactionValidationDTO.toEntity(transactionValidationDTO)
        transactionService.save(transaction)
        val invalidReasons = mutableListOf<String>()

        var transactionState = ALLOWED

        val suspiciousIp = transaction.ip?.let { ipService.findByIp(it) }
        if (suspiciousIp != null) {
            transactionState = PROHIBITED
            invalidReasons.add("ip")
        }

        val card = cardService.createIfNotFount(transaction.number!!)
        if (card.stolen) {
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


        val amountValidationState = Transaction.validateAmount(transaction, card)
        if (amountValidationState == PROHIBITED) {
            transactionState = PROHIBITED
            invalidReasons.add("amount")
        } else if (amountValidationState == MANUAL_PROCESSING && transactionState != PROHIBITED) {
            transactionState = MANUAL_PROCESSING
            invalidReasons.add("amount")
        }

        transaction.result = transactionState
        transactionService.save(transaction)

        return ResponseEntity.ok(
            ValidationResult(
                transactionState.toString(),
                if (transactionState == ALLOWED) "none" else invalidReasons.apply { sort() }.joinToString(", ")
            )
        )
    }

    @GetMapping("/history")
    @PreAuthorize("hasAuthority('LIST_TRANSACTION_PRIVILEGE')")
    fun listAllTransactions(): ResponseEntity<Any> =
        ResponseEntity.ok(transactionService.findAll().sortedBy { it.id }.map { TransactionDTO.fromEntity(it) })

    @GetMapping("/history/{number}")
    @PreAuthorize("hasAuthority('LIST_TRANSACTION_PRIVILEGE')")
    fun listAllTransactionsByCardNumber(
        @PathVariable @CardNumberConstraint number: String
    ): ResponseEntity<Any> {
        val transactions = transactionService.findAllByNumber(number).sortedBy { it.id }.map { TransactionDTO.fromEntity(it) }
        if (transactions.isEmpty()) {
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok(transactions)
    }

    @PutMapping("/transaction")
    @PreAuthorize("hasAuthority('UPDATE_TRANSACTION_PRIVILEGE')")
    fun updateTransactionFeedback(@RequestBody @Valid transactionFeedbackDto: TransactionFeedbackDto): ResponseEntity<Any> {
        val transaction =
            transactionService.findById(transactionFeedbackDto.transactionId!!) ?: return ResponseEntity.notFound()
                .build()

        if (transaction.feedback != null) {
            return ResponseEntity.status(409).build()
        }

        val card = cardService.createIfNotFount(transaction.number!!)

        val feedback = TransactionState.valueOf(transactionFeedbackDto.feedback!!)
        transaction.feedback = feedback
        when (transaction.result!!) {
            ALLOWED -> {
                when (feedback) {
                    MANUAL_PROCESSING -> {
                        card.maxAllowed = downLimit(card.maxAllowed, transaction.amount!!)
                    }

                    PROHIBITED -> {
                        card.maxAllowed = downLimit(card.maxAllowed, transaction.amount!!)
                        card.maxManualProcessing = downLimit(card.maxManualProcessing, transaction.amount!!)
                    }

                    else -> {
                        return ResponseEntity.unprocessableEntity().build()
                    }
                }
            }

            MANUAL_PROCESSING -> {
                when (feedback) {
                    ALLOWED -> {
                        card.maxAllowed = upLimit(card.maxAllowed, transaction.amount!!)
                    }

                    PROHIBITED -> {
                        card.maxManualProcessing = downLimit(card.maxManualProcessing, transaction.amount!!)
                    }

                    else -> {
                        return ResponseEntity.unprocessableEntity().build()
                    }
                }
            }

            PROHIBITED -> {
                when (feedback) {
                    ALLOWED -> {
                        card.maxAllowed = upLimit(card.maxAllowed, transaction.amount!!)
                        card.maxManualProcessing = upLimit(card.maxManualProcessing, transaction.amount!!)
                    }

                    MANUAL_PROCESSING -> {
                        card.maxManualProcessing = upLimit(card.maxManualProcessing, transaction.amount!!)
                    }

                    else -> {
                        return ResponseEntity.unprocessableEntity().build()
                    }
                }
            }

            else -> {
                return ResponseEntity.unprocessableEntity().build()
            }
        }

        cardService.save(card)
        transactionService.save(transaction)
        return ResponseEntity.ok().body(TransactionDTO.fromEntity(transaction))
    }

    fun upLimit(currentLimit: Int, transactionAmount: Int) = ceil((.8 * currentLimit + .2 * transactionAmount)).toInt()
    fun downLimit(currentLimit: Int, transactionAmount: Int) =
        ceil((.8 * currentLimit - .2 * transactionAmount)).toInt()
}