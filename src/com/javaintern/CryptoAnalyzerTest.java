package com.javaintern;

import com.javaintern.CryptoAnalyzer;

public class CryptoAnalyzerTest {

    public static void main(String[] args) {
        if (args.length < 3) {
            throw new RuntimeException("Number of arguments for program must be 3!");
        }
        CryptoAnalyzer.setSettings(args[0], args[1], args[2]);
        CryptoAnalyzer.analyze();
    }
}
