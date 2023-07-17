package antifraud.validation.util

import antifraud.validation.util.LuhnAlgorithm.checksum
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

private const val CARD_NUMBER_LENGTH = 16

@Target(AnnotationTarget.FIELD)
@MustBeDocumented
@Constraint(validatedBy = [CardNumberValidator::class])
annotation class CardNumberConstraint(
    val message: String = "{Invalid card number}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class CardNumberValidator : ConstraintValidator<CardNumberConstraint, String> {
    override fun isValid(value: String, context: ConstraintValidatorContext): Boolean {
        return isValidCardNumber(value)
    }
}

fun isValidCardNumber(cardNumber: String): Boolean {
    return if (cardNumber.length == CARD_NUMBER_LENGTH) {
        try {
            cardNumber.toLong()
            checksum(cardNumber)
        } catch (e: NumberFormatException) {
            false
        }
    } else false
}