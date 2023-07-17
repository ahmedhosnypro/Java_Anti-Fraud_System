package antifraud.util;

import java.util.ArrayList;

public class LuhnAlgorithm {
    private LuhnAlgorithm() {
    }

    private static final int CARD_NUMBER_LENGTH = 16;
    private static final int UTF_ZERO = 48;

    private static final int LUHN_MODULUS = 10;
    private static final int LUHN_MULTIPLY = 2;
    private static final int LUHN_SUBTRACTION = 9;
    private static final int LUHN_LIMIT = 9;

    static int createCardCheckSum(String cardNumBuilder) {
        int sum = sumLuhnDigits(cardNumBuilder);
        return sum % LUHN_MODULUS == 0 ? 0 : LUHN_MODULUS - sum % LUHN_MODULUS;
    }

    static boolean checksum(String inputCardNum) {
        int checksum = inputCardNum.charAt(inputCardNum.length() - 1) - UTF_ZERO;
        if (checksum == 0) {
            return checksum == sumLuhnDigits(inputCardNum) % 10;
        } else {
            return checksum == 10 - sumLuhnDigits(inputCardNum) % 10;
        }
    }

    private static int sumLuhnDigits(String cardNUm) {
        ArrayList<Integer> digits = new ArrayList<>();
        int i = 0;
        while (i < CARD_NUMBER_LENGTH - 1) {
            digits.add(cardNUm.charAt(i) - UTF_ZERO);
            i++;
        }

        for (int j = 0; j < digits.size(); j += 2) {
            int multi2Sub9 = digits.get(j) * LUHN_MULTIPLY <= LUHN_LIMIT ? digits.get(j) * LUHN_MULTIPLY : digits.get(j) * LUHN_MULTIPLY - LUHN_SUBTRACTION;
            digits.set(j, multi2Sub9);
        }
        return digits.stream().mapToInt(d -> d).sum();
    }
}
