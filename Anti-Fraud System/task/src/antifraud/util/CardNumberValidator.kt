package antifraud.util

object CardNumberValidator {
    private const val CARD_NUMBER_LENGTH = 16
    fun isValid(number: String): Boolean {
        return if (number.length == CARD_NUMBER_LENGTH) {
            try {
                number.toLong()
                LuhnAlgorithm.checksum(number)
            } catch (e: NumberFormatException) {
                false
            }
        } else false
    }
}
