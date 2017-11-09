package com.rmatushkin;

public class Main {

    public static void main(String[] args) {
        Downloader downloader = new Downloader(args);
        downloader.start();
    }
}
