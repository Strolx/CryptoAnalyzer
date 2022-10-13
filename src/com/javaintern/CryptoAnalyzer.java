package com.javaintern;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class CryptoAnalyzer {

    private static String pathToFile;
    private static String pathToFileForStaticAnalysis;
    private static int key;
    private static String operation;

    private final static String ENCODE_OPERATION = "encode";
    private final static String DECODE_OPERATION = "decode";
    private final static String BRUTE_FORCE_OPERATION = "bruteForce";

    private CryptoAnalyzer() {}

    public static void setPathToFile(String pathToFile) {
        CryptoAnalyzer.pathToFile = pathToFile;
    }

    public static void setFilePathForStaticAnalysis(String filePathForStaticAnalysis) {
        CryptoAnalyzer.pathToFileForStaticAnalysis = filePathForStaticAnalysis;
    }

    public static void setKey(int key) {
        CryptoAnalyzer.key = key;
    }

    public static void setOperation(String operation) {
        CryptoAnalyzer.operation = operation;
    }

    public static void setSettings(String operation, String pathToFile, String keyOrFilePathForStaticAnalysis) {
        setOperation(operation);
        checkOperation();
        setPathToFile(pathToFile);

        if (BRUTE_FORCE_OPERATION.equals(operation)) {
            setFilePathForStaticAnalysis(keyOrFilePathForStaticAnalysis);
        } else {
            setKey(Integer.parseInt(keyOrFilePathForStaticAnalysis));
        }
    }

    public static void analyze() {
        checkSettings();
        String text = getTextFromFile(pathToFile);
        Pair<String, Integer> result = null;
        String resultText;
        switch (operation) {
            case ENCODE_OPERATION:
                resultText = EncoderDecoder.encode(text, key);
                result = new Pair<>(resultText, 0);
                break;
            case DECODE_OPERATION:
                resultText = EncoderDecoder.decode(text, key);
                result = new Pair<>(resultText, 0);
                break;
            case BRUTE_FORCE_OPERATION:
                String textForStaticAnalysis = getTextFromFile(pathToFileForStaticAnalysis);
                result = StaticAnalyzer.bruteForce(text, textForStaticAnalysis);
                break;
        }
        if (result != null) {
            String outputFileName = getPathToOutputFile(result.getValue2());
            writeInFile(result.getValue1(), outputFileName);
        }
    }

    private static void writeInFile(String text, String outputFileName) {
        try (FileChannel fileChannel = FileChannel.open(Path.of(outputFileName), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            byte[] textInBytes = text.getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(textInBytes.length);
            buffer.put(textInBytes);
            buffer.flip();
            fileChannel.write(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getPathToOutputFile(Integer key) {
        String[] pathExt = pathToFile.split("\\.");
        String pathToFileWithoutExt = pathExt[0];
        String ext = pathExt[1];
        String operationName = "";
        if (ENCODE_OPERATION.equals(operation)) {
            operationName = "encoded";
        } else if (DECODE_OPERATION.equals(operation)
                || BRUTE_FORCE_OPERATION.equals(operation)) {
            operationName = "decoded";
        }
        String keyText = !BRUTE_FORCE_OPERATION.equals(operation) ? "" : " key-" + key;
        return String.format("%s(%s%s).%s", pathToFileWithoutExt, operationName, keyText, ext);
    }

    private static String getTextFromFile(String path) {
        StringBuilder text = new StringBuilder();
        try (FileChannel fileChannel = FileChannel.open(Path.of(path), StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (fileChannel.read(buffer) > 0) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    text.append((char) buffer.get());
                }
                buffer.flip();
                buffer.clear();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return text.toString();
    }

    private static void checkSettings() {
        checkOperation();
        checkPathToFile();
        checkPathToFileForStaticAnalysis();
    }

    private static void checkPathToFileForStaticAnalysis() {
        if (BRUTE_FORCE_OPERATION.equals(operation) && "".equals(pathToFileForStaticAnalysis)) {
            throw new RuntimeException("Path to file for static analysis is used for brute force method isn't set.");
        }
    }

    private static void checkPathToFile() {
        if ("".equals(pathToFile)) {
            throw new RuntimeException("Path to file isn't set.");
        }
    }

    private static void checkOperation() {
        if ("".equals(operation)) {
            throw new RuntimeException("Operation isn't set.");
        } else if (!ENCODE_OPERATION.equals(operation) && !DECODE_OPERATION.equals(operation) && !BRUTE_FORCE_OPERATION.equals(operation)) {
            throw new IllegalArgumentException("Not find such operation");
        }
    }



}
