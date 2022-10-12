package com.javaintern;

import java.util.HashMap;
import java.util.Map;

public final class StaticAnalyzer {

    private StaticAnalyzer() {}

    public static Pair<String, Integer> bruteForce(String decodedText, String textForStaticAnalyze) {

        Map<Character, Double> normalDistributionOfLettersInTextForStaticAnalyze = getNormalDistributionOfLetters(textForStaticAnalyze);
        Map<Character, Double> normalDistributionOfLettersInDecodedText = getNormalDistributionOfLetters(decodedText);

        int key = getKeyByDiffBetweenNormalDistributions(normalDistributionOfLettersInDecodedText, normalDistributionOfLettersInTextForStaticAnalyze);
        String resultText = EncoderDecoder.decode(decodedText, key);
        return new Pair<>(resultText, key);
    }

    private static int getKeyByDiffBetweenNormalDistributions(Map<Character, Double> normalDistributionOfText1, Map<Character, Double> normalDistributionOfText2) {

        int shift = 0;
        double diffBetweenDistributions = Double.MAX_VALUE;

        //Find minimum difference of normal distributions and shift at which this difference occurs
        for (int currentShift = 0; currentShift < EnglishAlphabet.NUMBER_OF_LETTERS && diffBetweenDistributions != 0; currentShift++) {

            double currentDiff = getDifferenceBetweenDistribution(normalDistributionOfText1, normalDistributionOfText2, currentShift);

            if (Double.compare(currentDiff, diffBetweenDistributions) < 0) {
                diffBetweenDistributions = currentDiff;
                shift = currentShift;
            }

        }

        return shift;
    }

    private static double getDifferenceBetweenDistribution(Map<Character, Double> normalDistributionOfText1, Map<Character, Double> normalDistributionOfText2, int shift) {
        double diff = 0.0;
        for (var letter : normalDistributionOfText1.keySet()) {
            char shiftedLetter = EnglishAlphabet.getShiftedLetter(letter, shift);
            diff += Math.abs(normalDistributionOfText1.get(shiftedLetter) - normalDistributionOfText2.get(letter));
        }
        return diff;
    }

    private static Map<Character, Double> getNormalDistributionOfLetters(String text) {

        HashMap<Character, Double> distributionOfLetters = new HashMap<>();
        double maxCount = 1.0;
        for (char letter : text.toCharArray()) {
            if (letter >= EnglishAlphabet.START_UPPERCASE_LETTER && letter <= EnglishAlphabet.END_UPPERCASE_LETTER
                    || letter >= EnglishAlphabet.START_LOWERCASE_LETTER && letter <= EnglishAlphabet.END_LOWERCASE_LETTER) {
                double count = distributionOfLetters.getOrDefault(letter, 0.0) + 1.0;
                distributionOfLetters.put(letter, count);
            }
        }

        for (double count : distributionOfLetters.values()) {
            if (count > maxCount) {
                maxCount = count;
            }
        }

        for (var entry : distributionOfLetters.entrySet()) {
            double normalizedValue = entry.getValue() / maxCount * 100.0;
            distributionOfLetters.put(entry.getKey(), normalizedValue);
        }

        for (char beginLetter = EnglishAlphabet.START_UPPERCASE_LETTER; beginLetter <= EnglishAlphabet.END_UPPERCASE_LETTER; beginLetter++) {
            distributionOfLetters.putIfAbsent(beginLetter, 0.0);
        }

        for (char beginLetter = EnglishAlphabet.START_LOWERCASE_LETTER; beginLetter <= EnglishAlphabet.END_LOWERCASE_LETTER; beginLetter++) {
            distributionOfLetters.putIfAbsent(beginLetter, 0.0);
        }

        return distributionOfLetters;
    }
}
