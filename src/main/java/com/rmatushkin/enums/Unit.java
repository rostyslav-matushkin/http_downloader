package com.rmatushkin.enums;

public enum Unit {
    KILOBYTE(1024),
    MEGABYTE(1048576);

    private int bytes;

    Unit(int bytes) {
        this.bytes = bytes;
    }

    public int getBytes() {
        return bytes;
    }
}
