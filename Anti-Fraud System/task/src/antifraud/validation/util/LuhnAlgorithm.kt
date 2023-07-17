package antifraud.validation.util

object LuhnAlgorithm {
    private const val CARD_NUMBER_LENGTH = 16
    private const val UTF_ZERO = 48
    private const val LUHN_MODULUS = 10
    private const val LUHN_MULTIPLY = 2
    private const val LUHN_SUBTRACTION = 9
    private const val LUHN_LIMIT = 9

    fun createCardCheckSum(cardNumBuilder: String): Int {
        val sum = sumLuhnDigits(cardNumBuilder)
        return if (sum % LUHN_MODULUS == 0) 0 else LUHN_MODULUS - sum % LUHN_MODULUS
    }

    fun checksum(inputCardNum: String): Boolean {
        val checksum = inputCardNum[inputCardNum.length - 1].code - UTF_ZERO
        return if (checksum == 0) {
            0 == sumLuhnDigits(inputCardNum) % 10
        } else {
            checksum == 10 - sumLuhnDigits(inputCardNum) % 10
        }
    }

    private fun sumLuhnDigits(cardNUm: String): Int {
        val digits = mutableListOf<Int>()
        var i = 0
        while (i < CARD_NUMBER_LENGTH - 1) {
            digits.add(cardNUm[i].code - UTF_ZERO)
            i++
        }
        var j = 0
        while (j < digits.size) {
            val multi2Sub9 =
                if (digits[j] * LUHN_MULTIPLY <= LUHN_LIMIT) digits[j] * LUHN_MULTIPLY
                else digits[j] * LUHN_MULTIPLY - LUHN_SUBTRACTION
            digits[j] = multi2Sub9
            j += 2
        }
        return digits.stream().mapToInt { it }.sum()
    }
}
