package com.javaintern;

public final class EnglishAlphabet {

    public static final int NUMBER_OF_LETTERS = 26;
    public static final char START_UPPERCASE_LETTER = 'A';
    public static final char END_UPPERCASE_LETTER = 'Z';
    public static final char START_LOWERCASE_LETTER = 'a';
    public static final char END_LOWERCASE_LETTER = 'z';

    public static char getShiftedLetter(char letter, int shift) {

        if (shift == 0) {
            return letter;
        }

        char newLetter = letter;
        int offset = 0;
        int posInAlphabet = 0;

        if (letter >= EnglishAlphabet.START_UPPERCASE_LETTER && letter <= EnglishAlphabet.END_UPPERCASE_LETTER) {
            offset = EnglishAlphabet.START_UPPERCASE_LETTER;
        } else if (letter >= EnglishAlphabet.START_LOWERCASE_LETTER && letter <= EnglishAlphabet.END_LOWERCASE_LETTER) {
            offset = EnglishAlphabet.START_LOWERCASE_LETTER;
        }

        if (offset != 0) {
            posInAlphabet = (letter - offset + shift) % EnglishAlphabet.NUMBER_OF_LETTERS;
            if (posInAlphabet < 0) {
                posInAlphabet += EnglishAlphabet.NUMBER_OF_LETTERS;
            }
            newLetter = (char) (posInAlphabet + offset);
        }

        return newLetter;
    }

    private EnglishAlphabet() {}
}
