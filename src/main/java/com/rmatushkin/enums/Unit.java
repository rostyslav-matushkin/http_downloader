package com.rmatushkin.enums;

public enum Unit {
    KILOBYTE(1024, "k"),
    MEGABYTE(1_048_576, "m");

    private int bytes;
    private String letter;

    Unit(int bytes, String letter) {
        this.bytes = bytes;
        this.letter = letter;
    }

    public int getBytes() {
        return bytes;
    }

    public String getLetter() {
        return letter;
    }
}
