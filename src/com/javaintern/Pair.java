package com.javaintern;

public final class Pair<T, U> {
    private T value1;
    private U value2;

    public Pair(T value1, U value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public T getValue1() {
        return value1;
    }

    public U getValue2() {
        return value2;
    }
}
