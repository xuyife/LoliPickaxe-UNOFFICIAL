package net.xuyifei.lolipickaxe.common.util;

public class RomanNumberUtil {
    private static final String[] ROMAN_NUMERALS = {
            "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"
    };

    private static final int[] ARABIC_VALUES = {
            1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1
    };

    public static String toRoman(int number) {
        if (number < 1 || number > 3999) {
            return String.valueOf(number);
        }

        StringBuilder roman = new StringBuilder();
        int remaining = number;

        for (int i = 0; i < ARABIC_VALUES.length; i++) {
            while (remaining >= ARABIC_VALUES[i]) {
                roman.append(ROMAN_NUMERALS[i]);
                remaining -= ARABIC_VALUES[i];
            }
        }

        return roman.toString();
    }
}
