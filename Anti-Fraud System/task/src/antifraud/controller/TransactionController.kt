package antifraud.controller

import antifraud.model.Transaction
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TransactionController {
    data class ValidationResult(val result: String)
    @PostMapping("/api/antifraud/transaction")
    fun validateTransaction(
        @RequestBody transaction: Transaction): ResponseEntity<Any> {
        if (transaction.amount < 1) {
            return ResponseEntity.badRequest().body("amount must be greater than 0")
        }
        return ResponseEntity.ok(ValidationResult(Transaction.validate(transaction).toString()))
    }
}

