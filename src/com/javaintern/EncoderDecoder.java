package com.javaintern;

public final class EncoderDecoder {

    private EncoderDecoder() {}

    public static String encode(String text, int key) {
        return encryptTextByCaesarСipher(text, key);
    }

    public static String decode(String text, int key) {
        return encryptTextByCaesarСipher(text, -key);
    }

    private static String encryptTextByCaesarСipher(String text, int key) {

        if (key == 0) {
            return text;
        }

        StringBuilder encryptedText = new StringBuilder();

        for (char letter : text.toCharArray()) {
            char shiftedLetter = EnglishAlphabet.getShiftedLetter(letter, key);
            encryptedText.append(shiftedLetter);
        }

        return encryptedText.toString();

    }

}
